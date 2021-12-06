package untamedwilds.util;

/*
The MIT License (MIT)

Copyright (c) 2020 Joseph Bettendorff a.k.a. "Commoble"

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import untamedwilds.UntamedWilds;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JSONLoader<T> extends JsonReloadListener {

    private static final Gson STANDARD_GSON = new Gson();
    private final Codec<T> codec;
    private final String folderName;

    protected Map<ResourceLocation, T> data = new HashMap<>();

    /**
     * Creates a data manager with a standard gson parser
     * @param folderName The name of the data folder that we will load from, vanilla folderNames are "recipes", "loot_tables", etc</br>
     * Jsons will be read from data/all_modids/folderName/all_jsons</br>
     * folderName can include subfolders, e.g. "some_mod_that_adds_lots_of_data_loaders/cheeses"
     * @param codec A codec to deserialize the json into your T, see javadocs above class
     */
    public JSONLoader(String folderName, Codec<T> codec) {
        this(folderName, codec, STANDARD_GSON);
    }

    /**
     * As above but with a custom GSON
     * @param folderName The name of the data folder that we will load from, vanilla folderNames are "recipes", "loot_tables", etc</br>
     * Jsons will be read from data/all_modids/folderName/all_jsons</br>
     * folderName can include subfolders, e.g. "some_mod_that_adds_lots_of_data_loaders/cheeses"
     * @param codec A codec to deserialize the json into your T, see javadocs above class
     * @param gson A gson for parsing the raw json data into JsonElements. JsonElement-to-T conversion will be done by the codec,
     * so gson type adapters shouldn't be necessary here
     */
    public JSONLoader(String folderName, Codec<T> codec, Gson gson) {
        super(gson, folderName);
        this.folderName = folderName;
        this.codec = codec;
    }

    /**
     * Get the data object for the given key
     * @param id A resourcelocation identifying a json; e.g. a json at data/some_modid/folderName/some_json.json has id "some_modid:some_json"
     * @return The java object that was deserializd from the json with the given ID, or null if no such object is associated with that ID
     */
    @Nullable
    public T getData(ResourceLocation id) {
        return this.data.get(id);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, IResourceManager resourceManager, IProfiler profiler) {
        UntamedWilds.LOGGER.info("Beginning loading of data for data loader: {}", this.folderName);
        this.data = this.mapValues(jsons);
        UntamedWilds.LOGGER.info("Data loader for {} loaded {} jsons", this.folderName, this.data.size());
    }

    private Map<ResourceLocation, T> mapValues(Map<ResourceLocation, JsonElement> inputs) {
        Map<ResourceLocation, T> newMap = new HashMap<>();

        for (Entry<ResourceLocation, JsonElement> entry : inputs.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement element = entry.getValue();
            // if we fail to parse json, log an error and continue
            // if we succeeded, add the resulting T to the map
            this.codec.decode(JsonOps.INSTANCE, element)
                    .get()
                    .ifLeft(result -> newMap.put(key, result.getFirst()))
                    .ifRight(partial -> UntamedWilds.LOGGER.error("Failed to parse data json for {} due to: {}", key.toString(), partial.message()));
        }

        return newMap;
    }
}
