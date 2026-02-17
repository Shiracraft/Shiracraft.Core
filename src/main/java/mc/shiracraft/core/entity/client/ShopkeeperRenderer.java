package mc.shiracraft.core.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mc.shiracraft.core.Core;
import mc.shiracraft.core.entity.ShopkeeperEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Simple humanoid renderer so the entity reads as a player-like NPC.
 * Later you can swap this to a proper PlayerRenderer + skin/profile system if you want true player skins.
 */
public class ShopkeeperRenderer extends HumanoidMobRenderer<ShopkeeperEntity, HumanoidModel<ShopkeeperEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "textures/entity/shopkeeper.png");

    public ShopkeeperRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(ShopkeeperEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(ShopkeeperEntity entity, PoseStack poseStack, float partialTickTime) {
        // Keep default (player-sized)
        super.scale(entity, poseStack, partialTickTime);
    }
}
