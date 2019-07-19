package sausage_core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;
import sausage_core.api.util.nbt.NBTs;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandData extends CommandBase {
	@Override
	public String getName() {
		return "data";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.sausage_core.data.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 2) throw new WrongUsageException("commands.sausage_core.data.usage");
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		String mode = args[0];
		args = ArrayUtils.remove(args, 0);
		switch (mode) {
			case "block": {
				if (args.length != 3) throw new WrongUsageException("commands.sausage_core.data.usage");
				BlockPos pos = parseBlockPos(sender, args, 0, false);
				World world = sender.getEntityWorld();
				if (!world.isBlockLoaded(pos)) throw new CommandException("commands.blockdata.outOfWorld");
				TileEntity tileentity = world.getTileEntity(pos);
				if (tileentity == null) throw new CommandException("commands.blockdata.notValid");
				NBTTagCompound nbt = tileentity.writeToNBT(new NBTTagCompound());
				player.sendMessage(new TextComponentTranslation("commands.sausage_core.data.success"));
				player.sendMessage(NBTs.highlight(nbt));
			}
			break;
			case "entity": {
				if (args.length != 1) throw new WrongUsageException("commands.sausage_core.data.usage");
				player.sendMessage(new TextComponentTranslation("commands.sausage_core.data.success"));
				player.sendMessage(NBTs.highlight(entityToNBT(getEntity(server, sender, args[0]))));
			}
			break;
			default:
				throw new WrongUsageException("commands.sausage_core.data.usage");
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length == 1) return getListOfStringsMatchingLastWord(args, "block", "entity");
		String mode = args[0];
		args = ArrayUtils.remove(args, 0);
		switch (mode) {
			case "entity":
				return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
			case "block":
				if (args.length > 0 && args.length <= 3) return getTabCompletionCoordinate(args, 0, targetPos);
		}
		return Collections.emptyList();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return args.length > 0 && args[0].equals("entity") && index == 1;
	}
}
