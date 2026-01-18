package net.runelite.mixins;

import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.rs.api.RSLoginType;
import net.runelite.rs.api.RSUsername;

@Mixin(RSUsername.class)
public abstract class UsernameMixin implements RSUsername {


	@Replace("<init>")
	private UsernameMixin(String username, RSLoginType type) {
		this.setName(username);
		if (username == null) {
			this.setCleanName((String)null);
		} else {
			String var3 = username;

			String var7;
			for(int var4 = username.indexOf("<title>"); var4 != -1; var4 = var7.indexOf("<title>")) {
				int var5 = var3.indexOf("</title>");
				String var6 = var3.substring(0, var4);
				var7 = var6 + var3.substring(var5 + "</title>".length());
				var3 = var7;
			}

			this.setCleanName(normalize(var3));
		}
	}


	@Inject
	private static final char[] field4062 = new char[]{' ', ' ', '_', '-', 'à', 'á', 'â', 'ä', 'ã', 'À', 'Á', 'Â', 'Ä', 'Ã', 'è', 'é', 'ê', 'ë', 'È', 'É', 'Ê', 'Ë', 'í', 'î', 'ï', 'Í', 'Î', 'Ï', 'ò', 'ó', 'ô', 'ö', 'õ', 'Ò', 'Ó', 'Ô', 'Ö', 'Õ', 'ù', 'ú', 'û', 'ü', 'Ù', 'Ú', 'Û', 'Ü', 'ç', 'Ç', 'ÿ', 'Ÿ', 'ñ', 'Ñ', 'ß'};

	@Inject
	private static final char[] field4063 = new char[]{'[', ']', '#'};

	@Inject
	private static String normalize(String text)
	{
		int var5 = 0;

		int var6;
		boolean var7;
		char var8;
		for(var6 = text.length(); var5 < var6; ++var5)
		{
			var8 = text.charAt(var5);
			var7 = var8 == 160 || var8 == ' ' || var8 == '_' || var8 == '-';
			if(!var7)
			{
				break;
			}
		}

		while(var6 > var5)
		{
			var8 = text.charAt(var6 - 1);
			var7 = var8 == 160 || var8 == ' ' || var8 == '_' || var8 == '-';
			if(!var7)
			{
				break;
			}

			--var6;
		}

		int var17 = var6 - var5;
		if(var17 >= 1 && var17 <= 120)
		{
			StringBuilder var15 = new StringBuilder(var17);

			for(int var9 = var5; var9 < var6; ++var9)
			{
				char var10 = text.charAt(var9);
				boolean var11;
				if(Character.isISOControl(var10))
				{
					var11 = false;
				}
				else if(var10 >= '0' && var10 <= '9' || var10 >= 'A' && var10 <= 'Z' || var10 >= 'a' && var10 <= 'z')
				{
					var11 = true;
				}
				else
				{
					char[] var16 = field4062;
					int var13 = 0;

					label89:
					while(true)
					{
						char var14;
						if(var13 >= var16.length)
						{
							var16 = field4063;

							for(var13 = 0; var13 < var16.length; ++var13)
							{
								var14 = var16[var13];
								if(var14 == var10)
								{
									var11 = true;
									break label89;
								}
							}

							var11 = false;
							break;
						}

						var14 = var16[var13];
						if(var14 == var10)
						{
							var11 = true;
							break;
						}

						++var13;
					}
				}

				if(var11)
				{
					char var12;
					switch(var10)
					{
						case ' ':
						case '-':
						case '_':
						case ' ':
							var12 = '_';
							break;
						case '#':
						case '[':
						case ']':
							var12 = var10;
							break;
						case 'À':
						case 'Á':
						case 'Â':
						case 'Ã':
						case 'Ä':
						case 'à':
						case 'á':
						case 'â':
						case 'ã':
						case 'ä':
							var12 = 'a';
							break;
						case 'Ç':
						case 'ç':
							var12 = 'c';
							break;
						case 'È':
						case 'É':
						case 'Ê':
						case 'Ë':
						case 'è':
						case 'é':
						case 'ê':
						case 'ë':
							var12 = 'e';
							break;
						case 'Í':
						case 'Î':
						case 'Ï':
						case 'í':
						case 'î':
						case 'ï':
							var12 = 'i';
							break;
						case 'Ñ':
						case 'ñ':
							var12 = 'n';
							break;
						case 'Ò':
						case 'Ó':
						case 'Ô':
						case 'Õ':
						case 'Ö':
						case 'ò':
						case 'ó':
						case 'ô':
						case 'õ':
						case 'ö':
							var12 = 'o';
							break;
						case 'Ù':
						case 'Ú':
						case 'Û':
						case 'Ü':
						case 'ù':
						case 'ú':
						case 'û':
						case 'ü':
							var12 = 'u';
							break;
						case 'ß':
							var12 = 'b';
							break;
						case 'ÿ':
						case 'Ÿ':
							var12 = 'y';
							break;
						default:
							var12 = Character.toLowerCase(var10);
					}

					if(var12 != 0)
					{
						var15.append(var12);
					}
				}
			}

			if(var15.length() == 0)
			{
				return null;
			}
			else
			{
				return var15.toString();
			}
		}
		return null;
	}
}
