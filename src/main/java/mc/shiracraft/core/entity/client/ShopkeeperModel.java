package mc.shiracraft.core.entity.client;

import mc.shiracraft.core.entity.ShopkeeperEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

/**
 * Extension point for a future custom model/animations.
 * For now this simply behaves like a vanilla humanoid model.
 */
public class ShopkeeperModel extends HumanoidModel<ShopkeeperEntity> {
    public ShopkeeperModel(ModelPart root) {
        super(root);
    }
}
