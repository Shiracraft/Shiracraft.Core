package mc.shiracraft.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mc.shiracraft.core.Core;

import java.io.*;

/**
 * Configuration will be loaded on startup and cannot be reloaded at runtime.
 * TODO: Generating and reading config files through json is likely not the most efficient or the correct way to do this.
 *       Consider switching to a different method in the future.
 */
public abstract class Config {

    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    protected String root = "config/" + Core.MOD_ID + "/";
    protected String extension = ".json";

    public void generateConfig() {
        this.reset();

        try {
            this.writeConfig();
        } catch (IOException e) {
            Core.LOGGER.error("Failed to generate config file for {}", this.getName());
        }
    }

    private File getConfigFile() {
        return new File(this.root + this.getName() + this.extension);
    }

    public abstract String getName();

    @SuppressWarnings("unchecked")
    public <T extends Config> T readConfig() {
        try {
            return (T) GSON.fromJson(new FileReader(this.getConfigFile()), this.getClass());
        } catch (FileNotFoundException e) {
            this.generateConfig();
        }

        return (T) this;
    }

    protected abstract void reset();

    public void writeConfig() throws IOException {
        File dir = new File(this.root);

        if (!dir.exists() && !dir.mkdirs()) return;
        if (!this.getConfigFile().exists() && !this.getConfigFile().createNewFile()) return;

        FileWriter writer = new FileWriter(this.getConfigFile());
        GSON.toJson(this, writer);
        writer.flush();
        writer.close();
    }

}