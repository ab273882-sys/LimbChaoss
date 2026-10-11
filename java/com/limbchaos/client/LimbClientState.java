package com.limbchaos.client;

/** Client-side cosmetic state for the local player's model. Server inventory/state is handled by commands. */
public final class LimbClientState {
    private LimbClientState() {}
    public static boolean leftArmMissing, rightArmMissing, leftLegMissing, rightLegMissing;

    public static boolean isMissing(String limb) {
        return switch (limb) {
            case "left_arm" -> leftArmMissing; case "right_arm" -> rightArmMissing;
            case "left_leg" -> leftLegMissing; case "right_leg" -> rightLegMissing; default -> false;
        };
    }
    public static void setMissing(String limb, boolean missing) {
        switch (limb) {
            case "left_arm" -> leftArmMissing = missing;
            case "right_arm" -> rightArmMissing = missing;
            case "left_leg" -> leftLegMissing = missing;
            case "right_leg" -> rightLegMissing = missing;
        }
    }
}
