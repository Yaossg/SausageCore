/**
 * Each plugin only runs when specific mod is loaded to prevent crash while finding classes
 * Logging by owner's modid and creating plugins by their class name.
 * Plugins shall implement {@link sausage_core.api.core.plugin.PluginCore} to receive constructor's parameter (the owner)
 *
 * @deprecated it is suggested that load plugins by annotations
 * @see sausage_core.api.annotation.LoadClass
 * */
package sausage_core.api.core.plugin;