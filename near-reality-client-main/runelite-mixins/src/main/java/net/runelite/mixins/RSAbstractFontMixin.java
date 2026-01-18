package net.runelite.mixins;

import net.runelite.ContentConstants;
import net.runelite.api.mixins.Copy;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.rs.api.RSAbstractFont;

/**
 * @author Jire
 */
@Mixin(RSAbstractFont.class)
public abstract class RSAbstractFontMixin implements RSAbstractFont {

	private static final String TARGET = "RuneScape";
	private static final String REPLACEMENT = ContentConstants.SERVER_NAME;

	@Copy("drawCentered")
	abstract void drawCentered(String var1, int var2, int var3, int var4, int var5);

	@Replace("drawCentered")
	public void rl$drawCentered(String var1, int var2, int var3, int var4, int var5) {
		if (var1 != null) {
			if (var1.contains(TARGET))
				var1 = var1.replace(TARGET, REPLACEMENT);

			drawCentered(var1, var2, var3, var4, var5);
		}
	}

}