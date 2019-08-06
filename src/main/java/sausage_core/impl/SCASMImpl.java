package sausage_core.impl;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sausage_core.api.annotation.AutoCall;
import sausage_core.api.annotation.AutoCall.When;
import sausage_core.api.annotation.InjectLogger;
import sausage_core.api.util.asm.ASMDataHelper;

import java.util.*;

public class SCASMImpl {
	private static final Logger logger = LogManager.getLogger("SCASMImpl");

	public static void init(ASMDataTable asmData) {
		logger.info("Start to initialize SausageCoreASM");
		injectLogger(asmData);
		autoCall(asmData);
		logger.info("Complete");
	}

	public static void injectLogger(ASMDataTable asmData) {
		Set<ASMDataTable.ASMData> all = asmData.getAll(InjectLogger.class.getName());
		for (ASMDataTable.ASMData data : all) try {
			Class<?> modClass = Class.forName(data.getClassName());
			String name = (String) data.getAnnotationInfo().getOrDefault("value", "");
			if (name.isEmpty() && modClass.isAnnotationPresent(Mod.class))
				name = modClass.getAnnotation(Mod.class).modid();
			ASMDataHelper.setField(data, LogManager.getLogger(name));
			logger.info("processed @InjectLogger for {}#{}", data.getClassName(), data.getObjectName());
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private static final Map<When, Set<Runnable>> map = new EnumMap<>(When.class);

	public static void autoCall(ASMDataTable asmData) {
		Set<ASMDataTable.ASMData> all = asmData.getAll(AutoCall.class.getName());
		for (ASMDataTable.ASMData data : all) try {
			AutoCall info = ASMDataHelper.toAnnotation(AutoCall.class, data.getAnnotationInfo());
			for (Side side : info.side()) if (FMLCommonHandler.instance().getSide() == side)
				for (When when : info.when())
					map.computeIfAbsent(when, $ -> new HashSet<>()).addAll(ASMDataHelper.getField(data));
			logger.info("processed @AutoCall for {}#{}", data.getClassName(), data.getObjectName());
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void call(When when) {
		map.getOrDefault(when, Collections.emptySet()).forEach(Runnable::run);
		logger.info("@AutoCall(when = {}, at = {}) is called", when, FMLCommonHandler.instance().getSide());
	}
}
