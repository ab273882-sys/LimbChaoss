package com.limbchaos.mixin;

import com.limbchaos.client.LimbClientState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Hides the matching body parts in the client model. First version uses local-client cosmetic state. */
@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin {
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftLeg;
    @Shadow @Final public ModelPart rightLeg;

    @Inject(method = "setAngles", at = @At("TAIL"))
    private void limbchaos$hideMissingLimbs(PlayerEntityRenderState state, CallbackInfo ci) {
        leftArm.visible = !LimbClientState.leftArmMissing;
        rightArm.visible = !LimbClientState.rightArmMissing;
        leftLeg.visible = !LimbClientState.leftLegMissing;
        rightLeg.visible = !LimbClientState.rightLegMissing;
    }
}
