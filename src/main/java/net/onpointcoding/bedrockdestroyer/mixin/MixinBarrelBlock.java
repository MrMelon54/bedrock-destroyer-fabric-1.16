package net.onpointcoding.bedrockdestroyer.mixin;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.onpointcoding.bedrockdestroyer.BedrockDestroyer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrelBlock.class)
public abstract class MixinBarrelBlock {
    @Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;", ordinal = 0))
    private void wrapOnUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (BedrockDestroyer.getInstance().shouldIgnorePlayer(player)) return;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BarrelBlockEntity) {
            BarrelBlockEntity barrelBlockEntity = (BarrelBlockEntity) blockEntity;
            int size = barrelBlockEntity.size();
            for (int i = 0; i < size; i++) {
                ItemStack stack = barrelBlockEntity.getStack(i);
                if (BedrockDestroyer.getInstance().shouldDestroyItemStack(stack)) {
                    barrelBlockEntity.setStack(i, BedrockDestroyer.getInstance().createEmptyStack());
                }
            }
        }
    }
}
