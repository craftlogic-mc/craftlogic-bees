package ru.craftlogic.bees.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import ru.craftlogic.api.BeesAPI;
import ru.craftlogic.api.BeesItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class ModelHoneycomb implements IModel {
    public static final ModelResourceLocation LOCATION = new ModelResourceLocation(new ResourceLocation(BeesAPI.MOD_ID, "honeycomb"), "inventory");

    public static final IModel MODEL = new ModelHoneycomb();

    @Nullable
    private final ResourceLocation backgroundTexture;
    @Nullable
    private final ResourceLocation[] slotTextures;

    public ModelHoneycomb() {
        backgroundTexture = null;
        slotTextures = null;
    }

    public ModelHoneycomb(ResourceLocation backgroundTexture, ResourceLocation[] slotTextures) {
        this.backgroundTexture = backgroundTexture;
        this.slotTextures = slotTextures;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures) {
        return process(textures);
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        ResourceLocation background = new ResourceLocation(BeesAPI.MOD_ID, "items/honeycomb_empty");
        ResourceLocation[] slots = new ResourceLocation[7];
        for (int i = 0; i < 7; i++) {
            if (customData.containsKey("slot" + (i + 1))) {
                slots[i] = new ResourceLocation(BeesAPI.MOD_ID, "items/honeycomb_slot" + (i + 1));
            } else {
                slots[i] = new ResourceLocation(BeesAPI.MOD_ID, "items/honeycomb_slot_empty");
            }
        }
        return new ModelHoneycomb(background, slots);
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
        builder.add(new ResourceLocation(BeesAPI.MOD_ID, "items/honeycomb_empty"));
        builder.add(new ResourceLocation(BeesAPI.MOD_ID, "items/honeycomb_slot_empty"));
        for (int i = 0; i < 7; i++) {
            builder.add(new ResourceLocation(BeesAPI.MOD_ID, "items/honeycomb_slot" + (i + 1)));
        }
        return builder.build();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        TextureAtlasSprite particle = null;

        if (backgroundTexture != null && slotTextures != null) {
            ImmutableList.Builder<ResourceLocation> texBuilder = ImmutableList.builder();
            texBuilder.add(backgroundTexture);
            for (ResourceLocation slotTexture : slotTextures) {
                if (slotTexture != null) {
                    texBuilder.add(slotTexture);
                }
            }
            ImmutableList<ResourceLocation> textures = texBuilder.build();
            IBakedModel model = new ItemLayerModel(textures).bake(state, format, bakedTextureGetter);
            builder.addAll(model.getQuads(null, null, 0));
            particle = model.getParticleTexture();
        }

        return new BakedModel(this, builder.build(), format, Maps.immutableEnumMap(transformMap), particle, Maps.newHashMap());
    }

    public enum Loader implements ICustomModelLoader {
        INSTANCE;

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation.getNamespace().equals(BeesAPI.MOD_ID)
                && modelLocation.getPath().equals("models/item/honeycomb");
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) {
            return MODEL;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {}
    }

    private static final class OverrideHandler extends ItemOverrideList {
        public static final OverrideHandler INSTANCE = new OverrideHandler();

        private OverrideHandler() {
            super(ImmutableList.of());
        }

        @Override
        @Nonnull
        public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
            if (stack.getItem() != BeesItems.HONEYCOMB) {
                return originalModel;
            }

            BakedModel model = (BakedModel) originalModel;

            int mask = 0;

            NBTTagCompound compound = stack.getTagCompound();

            if (compound != null && compound.hasKey("slots", 11)) {
                int[] slots = compound.getIntArray("slots");
                for (int i = 0; i < slots.length; i++) {
                    int s = slots[i];
                    if (s != 0) {
                        mask |= 1 << i;
                    }
                }
            }

            String key = String.valueOf(mask);

            if (!model.cache.containsKey(key)) {
                ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
                builder.put("wax", "true");
                for (int i = 0; i < 7; i++) {
                    if (((mask >> i) & 1) != 0) {
                        builder.put("slot" + (i + 1), "true");
                    }
                }
                IModel parent = model.parent.retexture(builder.build());
                Function<ResourceLocation, TextureAtlasSprite> textureGetter =
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                IBakedModel bakedModel = parent.bake(new SimpleModelState(model.getTransforms()), model.format, textureGetter);
                model.cache.put(key, bakedModel);
                return bakedModel;
            }
            return model.cache.get(key);
        }
    }

    private static final TRSRTransformation flipX = new TRSRTransformation(null, null, new Vector3f(-1F, 1F, 1F), null);

    private static final class BakedModel extends BakedItemModel {
        protected final IModel parent;
        private final ImmutableMap<TransformType, TRSRTransformation> transforms;
        protected final Map<String, IBakedModel> cache;
        protected final VertexFormat format;

        public BakedModel(IModel parent, ImmutableList<BakedQuad> quads, VertexFormat format,
                          ImmutableMap<TransformType, TRSRTransformation> transforms, TextureAtlasSprite particle,
                          Map<String, IBakedModel> cache) {
            super(quads, particle, transforms, OverrideHandler.INSTANCE);
            this.format = format;
            this.parent = parent;
            this.transforms = getTransforms();
            this.cache = cache;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
            return PerspectiveMapWrapper.handlePerspective(this, transforms, cameraTransformType);
        }

        protected ImmutableMap<TransformType, TRSRTransformation> getTransforms() {
            ImmutableMap.Builder<TransformType, TRSRTransformation> builder = ImmutableMap.builder();

            builder.put(TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5F));
            builder.put(TransformType.HEAD, get(0, 13, 7, 0, 180, 0, 1));
            builder.put(TransformType.FIXED, get(0, 0, 0, 0, 180, 0, 1));
            builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 3, 1, 0, 0, 0, 0.55F));
            builder.put(TransformType.THIRD_PERSON_LEFT_HAND, leftify(get(0, 3, 1, 0, 0, 0, 0.55F)));
            builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(1.13F, 3.2F, 1.13F, 0, -90, 25, 0.68F));
            builder.put(TransformType.FIRST_PERSON_LEFT_HAND, leftify(get(1.13F, 3.2F, 1.13F, 0, -90, 25, 0.68F)));
            return builder.build();
        }

        protected TRSRTransformation leftify(TRSRTransformation right) {
            return TRSRTransformation.blockCenterToCorner(flipX.compose(TRSRTransformation.blockCornerToCenter(right)).compose(flipX));
        }

        protected TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s) {
            return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(tx / 16, ty / 16, tz / 16), TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)), new Vector3f(s, s, s), null));
        }
    }
}
