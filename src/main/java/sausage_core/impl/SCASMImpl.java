package sausage_core.impl;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sausage_core.api.annotation.AutoCall;
import sausage_core.api.annotation.InjectLogger;
import sausage_core.api.annotation.LoadClass;
import sausage_core.api.util.asm.ASMDataHelper;

import java.util.*;

public class SCASMImpl {
	private static final Logger logger = LogManager.getLogger("SCASMImpl");

	public static void init(ASMDataTable asmData) {
		logger.info("Start to initialize SausageCoreASM");
		injectLogger(asmData);
		autoCall(asmData);
		loadClass(asmData);
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

	private static final Map<AutoCall.When, Set<Runnable>> map = new EnumMap<>(AutoCall.When.class);

	public static void autoCall(ASMDataTable asmData) {
		Set<ASMDataTable.ASMData> all = asmData.getAll(AutoCall.class.getName());
		for (ASMDataTable.ASMData data : all) try {
			AutoCall info = ASMDataHelper.toAnnotation(AutoCall.class, data.getAnnotationInfo());
			for (Side side : info.side()) if (FMLCommonHandler.instance().getSide() == side)
				for (AutoCall.When when : info.when())
					map.computeIfAbsent(when, $ -> new HashSet<>()).addAll(ASMDataHelper.getField(data));
			logger.info("processed @AutoCall for {}#{}", data.getClassName(), data.getObjectName());
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void call(AutoCall.When when) {
		map.getOrDefault(when, Collections.emptySet()).forEach(Runnable::run);
		logger.info("@AutoCall(when = {}, at = {}) is called", when, FMLCommonHandler.instance().getSide());
	}

	public static void loadClass(ASMDataTable asmData) {
		Set<ASMDataTable.ASMData> all = asmData.getAll(LoadClass.class.getName());
		for (ASMDataTable.ASMData data : all) try {
			LoadClass info = ASMDataHelper.toAnnotation(LoadClass.class, data.getAnnotationInfo());
			for (Side side : info.side()) if (FMLCommonHandler.instance().getSide() == side && Loader.isModLoaded(info.modRequired()))
				for (LoadClass.When when : info.when()) {
					map.computeIfAbsent(when.intern(), $ -> new HashSet<>()).add(() -> {
						try {
							Class<?> clazz = Class.forName(data.getClassName());
							logger.info("loaded class: {}", clazz.getName());
							if (info.construct()) {
								clazz.newInstance();
								logger.info("    then constructed an instance of it");
							}
						} catch (Exception e) {
							logger.error(e);
						}

					});
				}
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
