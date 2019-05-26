package me.vankka.placeholdersplus.module.mvdwplacholderapimodule;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.internal.PlaceholderPack;
import be.maximvdw.placeholderapi.internal.PlaceholderPlugin;
import me.vankka.placeholdersplus.common.model.PlaceholderLookupResult;
import me.vankka.placeholdersplus.common.model.PlaceholderReplacer;
import me.vankka.placeholdersplus.common.model.PlaceholdersPlusModule;
import me.vankka.placeholdersplus.common.model.PlaceholdersPlusPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class MVdWPlaceholderAPIModule extends PlaceholdersPlusModule implements PlaceholderReplacer, be.maximvdw.placeholderapi.PlaceholderReplacer {

    @Override
    public void enable(PlaceholdersPlusPlugin plugin) {
        placeholderHook.addPlaceholderReplacers(this);

        PlaceholderAPI.registerPlaceholder((Plugin) plugin, "placeholdersplus_*", this);

        //((PlaceholderAPI) Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI"))
        //        .registerMVdWPlugin((Plugin) plugin, this);
    }

    @Override
    public void disable(PlaceholdersPlusPlugin plugin) {
        placeholderHook.removePlaceholderReplacers(this);
    }

    @Override
    public PlaceholderLookupResult lookup(String placeholder, List<Object> extraObjects) {
        if (!placeholder.startsWith("mvdwplaceholderapi_"))
            return new PlaceholderLookupResult(PlaceholderLookupResult.ResultType.UNKNOWN_PLACEHOLDER);

        OfflinePlayer offlinePlayer = extraObjects.stream()
                .filter(object -> object instanceof OfflinePlayer)
                .map(object -> (OfflinePlayer) object)
                .findAny().orElse(null);

        return output(PlaceholderAPI.replacePlaceholders(offlinePlayer,
                "%" + placeholder.replace("mvdwplaceholderapi_", "") + "%"));
    }

    private PlaceholderLookupResult output(String output) {
        if (output == null)
            return new PlaceholderLookupResult(PlaceholderLookupResult.ResultType.UNKNOWN_PLACEHOLDER);
        else if (output.isEmpty())
            return new PlaceholderLookupResult(PlaceholderLookupResult.ResultType.DATA_NOT_LOADED);
        else
            return new PlaceholderLookupResult(output);
    }

    @Override
    public String onPlaceholderReplace(PlaceholderReplaceEvent event) {
        String placeholder = event.getPlaceholder().replace("placeholdersplus_", "");
        //System.out.println(placeholder + " <-");

        return placeholderHook.getPlaceholderReplacement(placeholder,
                event.getPlayer() != null ? event.getPlayer() : event.getOfflinePlayer()).getReplacement();
    }
}
