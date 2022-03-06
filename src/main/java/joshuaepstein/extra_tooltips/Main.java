package joshuaepstein.extra_tooltips;

import com.google.common.collect.Lists;
import joshuaepstein.extra_tooltips.config.ModConfigs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.item.Item;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main implements ModInitializer {
	public static final String MODID = "extra_tooltips";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		ModConfigs.register();

		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
			dispatcher.register(CommandManager.literal("reloadtooltips").executes(context -> {
				try {
					ModConfigs.register();
				} catch(Exception e){
					e.printStackTrace();
					throw e;
				}
				return 1;
			}));
		}));


		ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, list) -> {
			if (itemStack.isEmpty()) {
				return;
			}
			Item item = itemStack.getItem();
			ModConfigs.TOOLTIPS.getTooltipString(item).ifPresent((str) -> {
				List<String> added = Lists.reverse(Lists.newArrayList(str.split("\n")));
				for(String newStr : added){
					list.add(1, new LiteralText(newStr).formatted(Formatting.GRAY));
				}
			});
		});

	}

}
