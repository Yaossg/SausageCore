package yaossg.mod.sausage_core.api.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Conversions {
    public static Item block2item(Block block) {
        return Item.getItemFromBlock(block);
    }
    public static Block item2block(Item item) {
        return Block.getBlockFromItem(item);
    }
    public static ItemStack state2stack(IBlockState state) {
        Item item = block2item(state.getBlock());
        return new ItemStack(item, 1, item.getHasSubtypes() ? state.getBlock().getMetaFromState(state) : 0);
    }
    @SuppressWarnings("deprecation")
    public static IBlockState stack2state(ItemStack stack) {
        Block block = item2block(stack.getItem());
        return block.getStateFromMeta(stack.getMetadata());
    }
    public static class To {
        public static Item item(Item item) {
            return item;
        }
        public static Item item(Block block) {
            return block2item(block);
        }
        public static Item item(ItemStack stack) {
            return stack.getItem();
        }
        public static Item item(IBlockState state) {
            return block2item(block(state));
        }
        public static Block block(Item item) {
            return item2block(item);
        }
        public static Block block(Block block) {
            return block;
        }
        public static Block block(ItemStack stack) {
            return item2block(item(stack));
        }
        public static Block block(IBlockState state) {
            return state.getBlock();
        }
    }
}
