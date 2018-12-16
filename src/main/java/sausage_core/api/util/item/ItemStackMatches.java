package sausage_core.api.util.item;

import com.google.common.collect.Collections2;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import sausage_core.api.util.common.IEqualityComparator;

import java.util.List;

public class ItemStackMatches {
    public static NonNullList<ItemStack> merge(List<ItemStack> stacks) {
        PortableItemStackHandler handler = new PortableItemStackHandler(stacks.size());
        for (ItemStack stack : stacks) {
            ItemStack each = stack.copy();
            for (int i = 0; !each.isEmpty() && i < stacks.size(); ++i)
                each = handler.insertItem(i, each, false);
        }
        NonNullList<ItemStack> ret = handler.copyStacks();
        ret.removeIf(ItemStack::isEmpty);
        return ret;
    }

    public static ItemStack[] match(IngredientStack[] ins, List<ItemStack> stacks) {
        if(ins.length != stacks.size())
            return null;
        for (List<ItemStack> permutation : Collections2.permutations(stacks))
            if(singleMatch(ins, permutation)) {
                ItemStack[] copy = permutation.toArray(new ItemStack[0]);
                for (int i = 0; i < copy.length; ++i) copy[i].setCount(ins[i].getCount());
                return copy;
            }
        return null;
    }

    public static boolean singleMatch(IngredientStack[] ins, List<ItemStack> stacks) {
        for (int i = 0; i < ins.length; ++i)
            if(!ins[i].test(stacks.get(i)))
                return false;
        return true;
    }

    public static void remove(PortableItemStackHandler handler, ItemStack[] stacks, IEqualityComparator<ItemStack> equal) {
        for (ItemStack stack : stacks) {
            ItemStack in = stack.copy();
            for (IItemStackSlotView storage : handler)
                if(storage.compare(equal).test(in)) {
                    if(in.getCount() <= storage.getCount()) {
                        storage.shrink(in.getCount());
                        break;
                    } else {
                        in.shrink(storage.getCount());
                        storage.setCount(0);
                    }
                }
        }
    }
}
