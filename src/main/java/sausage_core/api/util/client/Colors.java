package sausage_core.api.util.client;

import java.lang.reflect.Field;

/**
 * The list of standard 148 SVG colors
 * <p>Including 139 different colors with 9 alias:
 * <ul>
 * <li>[optional-prefix]gray = [optional-prefix]grey (7 alias in total)</li>
 * <li>magenta = fuchsia, cyan = aqua (without prefix only, the former ones are used in other names)</li>
 * </ul>
 * <p>More colors are coming soon
 */
public final class Colors {
	/**
	 * The color alice_blue with an RGB value of #F0F8FF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F0F8FF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int ALICE_BLUE = 0xFFF0F8FF;
	/**
	 * The color antique_white with an RGB value of #FAEBD7
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FAEBD7;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int ANTIQUE_WHITE = 0xFFFAEBD7;
	/**
	 * The color aqua with an RGB value of #00FFFF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#00FFFF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int AQUA = 0xFF00FFFF;
	/**
	 * The color aquamarine with an RGB value of #7FFFD4
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#7FFFD4;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int AQUAMARINE = 0xFF7FFFD4;
	/**
	 * The color azure with an RGB value of #F0FFFF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F0FFFF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int AZURE = 0xFFF0FFFF;
	/**
	 * The color beige with an RGB value of #F5F5DC
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F5F5DC;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int BEIGE = 0xFFF5F5DC;
	/**
	 * The color bisque with an RGB value of #FFE4C4
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFE4C4;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int BISQUE = 0xFFFFE4C4;
	/**
	 * The color black with an RGB value of #000000
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#000000;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int BLACK = 0xFF000000;
	/**
	 * The color blanched_almond with an RGB value of #FFEBCD
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFEBCD;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int BLANCHED_ALMOND = 0xFFFFEBCD;
	/**
	 * The color blue with an RGB value of #0000FF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#0000FF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int BLUE = 0xFF0000FF;
	/**
	 * The color blue_violet with an RGB value of #8A2BE2
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#8A2BE2;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int BLUE_VIOLET = 0xFF8A2BE2;
	/**
	 * The color brown with an RGB value of #A52A2A
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#A52A2A;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int BROWN = 0xFFA52A2A;
	/**
	 * The color burly_wood with an RGB value of #DEB887
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#DEB887;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int BURLY_WOOD = 0xFFDEB887;
	/**
	 * The color cadet_blue with an RGB value of #5F9EA0
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#5F9EA0;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int CADET_BLUE = 0xFF5F9EA0;
	/**
	 * The color chartreuse with an RGB value of #7FFF00
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#7FFF00;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int CHARTREUSE = 0xFF7FFF00;
	/**
	 * The color chocolate with an RGB value of #D2691E
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#D2691E;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int CHOCOLATE = 0xFFD2691E;
	/**
	 * The color coral with an RGB value of #FF7F50
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF7F50;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int CORAL = 0xFFFF7F50;
	/**
	 * The color cornflower_blue with an RGB value of #6495ED
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#6495ED;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int CORNFLOWER_BLUE = 0xFF6495ED;
	/**
	 * The color cornsilk with an RGB value of #FFF8DC
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFF8DC;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int CORNSILK = 0xFFFFF8DC;
	/**
	 * The color crimson with an RGB value of #DC143C
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#DC143C;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int CRIMSON = 0xFFDC143C;
	/**
	 * The color cyan with an RGB value of #00FFFF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#00FFFF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int CYAN = 0xFF00FFFF;
	/**
	 * The color dark_blue with an RGB value of #00008B
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#00008B;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_BLUE = 0xFF00008B;
	/**
	 * The color dark_cyan with an RGB value of #008B8B
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#008B8B;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_CYAN = 0xFF008B8B;
	/**
	 * The color dark_goldenrod with an RGB value of #B8860B
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#B8860B;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_GOLDENROD = 0xFFB8860B;
	/**
	 * The color dark_gray with an RGB value of #A9A9A9
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#A9A9A9;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_GRAY = 0xFFA9A9A9;
	/**
	 * The color dark_green with an RGB value of #006400
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#006400;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_GREEN = 0xFF006400;
	/**
	 * The color dark_grey with an RGB value of #A9A9A9
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#A9A9A9;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_GREY = 0xFFA9A9A9;
	/**
	 * The color dark_khaki with an RGB value of #BDB76B
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#BDB76B;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_KHAKI = 0xFFBDB76B;
	/**
	 * The color dark_magenta with an RGB value of #8B008B
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#8B008B;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_MAGENTA = 0xFF8B008B;
	/**
	 * The color dark_olive_green with an RGB value of #556B2F
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#556B2F;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_OLIVE_GREEN = 0xFF556B2F;
	/**
	 * The color dark_orange with an RGB value of #FF8C00
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF8C00;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_ORANGE = 0xFFFF8C00;
	/**
	 * The color dark_orchid with an RGB value of #9932CC
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#9932CC;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_ORCHID = 0xFF9932CC;
	/**
	 * The color dark_red with an RGB value of #8B0000
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#8B0000;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_RED = 0xFF8B0000;
	/**
	 * The color dark_salmon with an RGB value of #E9967A
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#E9967A;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_SALMON = 0xFFE9967A;
	/**
	 * The color dark_sea_green with an RGB value of #8FBC8F
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#8FBC8F;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_SEA_GREEN = 0xFF8FBC8F;
	/**
	 * The color dark_slate_blue with an RGB value of #483D8B
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#483D8B;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_SLATE_BLUE = 0xFF483D8B;
	/**
	 * The color dark_slate_gray with an RGB value of #2F4F4F
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#2F4F4F;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_SLATE_GRAY = 0xFF2F4F4F;
	/**
	 * The color dark_slate_grey with an RGB value of #2F4F4F
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#2F4F4F;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_SLATE_GREY = 0xFF2F4F4F;
	/**
	 * The color dark_turquoise with an RGB value of #00CED1
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#00CED1;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_TURQUOISE = 0xFF00CED1;
	/**
	 * The color dark_violet with an RGB value of #9400D3
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#9400D3;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DARK_VIOLET = 0xFF9400D3;
	/**
	 * The color deep_pink with an RGB value of #FF1493
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF1493;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DEEP_PINK = 0xFFFF1493;
	/**
	 * The color deep_sky_blue with an RGB value of #00BFFF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#00BFFF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DEEP_SKY_BLUE = 0xFF00BFFF;
	/**
	 * The color dim_gray with an RGB value of #696969
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#696969;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DIM_GRAY = 0xFF696969;
	/**
	 * The color dim_grey with an RGB value of #696969
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#696969;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DIM_GREY = 0xFF696969;
	/**
	 * The color dodger_blue with an RGB value of #1E90FF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#1E90FF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int DODGER_BLUE = 0xFF1E90FF;
	/**
	 * The color firebrick with an RGB value of #B22222
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#B22222;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int FIREBRICK = 0xFFB22222;
	/**
	 * The color floral_white with an RGB value of #FFFAF0
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFAF0;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int FLORAL_WHITE = 0xFFFFFAF0;
	/**
	 * The color forest_green with an RGB value of #228B22
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#228B22;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int FOREST_GREEN = 0xFF228B22;
	/**
	 * The color fuchsia with an RGB value of #FF00FF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF00FF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int FUCHSIA = 0xFFFF00FF;
	/**
	 * The color gainsboro with an RGB value of #DCDCDC
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#DCDCDC;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int GAINSBORO = 0xFFDCDCDC;
	/**
	 * The color ghost_white with an RGB value of #F8F8FF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F8F8FF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int GHOST_WHITE = 0xFFF8F8FF;
	/**
	 * The color gold with an RGB value of #FFD700
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFD700;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int GOLD = 0xFFFFD700;
	/**
	 * The color goldenrod with an RGB value of #DAA520
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#DAA520;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int GOLDENROD = 0xFFDAA520;
	/**
	 * The color gray with an RGB value of #808080
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#808080;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int GRAY = 0xFF808080;
	/**
	 * The color green with an RGB value of #008000
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#008000;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int GREEN = 0xFF008000;
	/**
	 * The color green_yellow with an RGB value of #ADFF2F
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#ADFF2F;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int GREEN_YELLOW = 0xFFADFF2F;
	/**
	 * The color grey with an RGB value of #808080
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#808080;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int GREY = 0xFF808080;
	/**
	 * The color honeydew with an RGB value of #F0FFF0
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F0FFF0;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int HONEYDEW = 0xFFF0FFF0;
	/**
	 * The color hot_pink with an RGB value of #FF69B4
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF69B4;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int HOT_PINK = 0xFFFF69B4;
	/**
	 * The color indian_red with an RGB value of #CD5C5C
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#CD5C5C;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int INDIAN_RED = 0xFFCD5C5C;
	/**
	 * The color indigo with an RGB value of #4B0082
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#4B0082;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int INDIGO = 0xFF4B0082;
	/**
	 * The color ivory with an RGB value of #FFFFF0
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFFF0;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int IVORY = 0xFFFFFFF0;
	/**
	 * The color khaki with an RGB value of #F0E68C
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F0E68C;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int KHAKI = 0xFFF0E68C;
	/**
	 * The color lavender with an RGB value of #E6E6FA
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#E6E6FA;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LAVENDER = 0xFFE6E6FA;
	/**
	 * The color lavender_blush with an RGB value of #FFF0F5
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFF0F5;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LAVENDER_BLUSH = 0xFFFFF0F5;
	/**
	 * The color lawn_green with an RGB value of #7CFC00
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#7CFC00;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LAWN_GREEN = 0xFF7CFC00;
	/**
	 * The color lemon_chiffon with an RGB value of #FFFACD
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFACD;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LEMON_CHIFFON = 0xFFFFFACD;
	/**
	 * The color light_blue with an RGB value of #ADD8E6
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#ADD8E6;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_BLUE = 0xFFADD8E6;
	/**
	 * The color light_coral with an RGB value of #F08080
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F08080;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_CORAL = 0xFFF08080;
	/**
	 * The color light_cyan with an RGB value of #E0FFFF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#E0FFFF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_CYAN = 0xFFE0FFFF;
	/**
	 * The color light_goldenrod_yellow with an RGB value of #FAFAD2
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FAFAD2;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_GOLDENROD_YELLOW = 0xFFFAFAD2;
	/**
	 * The color light_gray with an RGB value of #D3D3D3
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#D3D3D3;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_GRAY = 0xFFD3D3D3;
	/**
	 * The color light_green with an RGB value of #90EE90
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#90EE90;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_GREEN = 0xFF90EE90;
	/**
	 * The color light_grey with an RGB value of #D3D3D3
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#D3D3D3;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_GREY = 0xFFD3D3D3;
	/**
	 * The color light_pink with an RGB value of #FFB6C1
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFB6C1;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_PINK = 0xFFFFB6C1;
	/**
	 * The color light_salmon with an RGB value of #FFA07A
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFA07A;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_SALMON = 0xFFFFA07A;
	/**
	 * The color light_sea_green with an RGB value of #20B2AA
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#20B2AA;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_SEA_GREEN = 0xFF20B2AA;
	/**
	 * The color light_sky_blue with an RGB value of #87CEFA
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#87CEFA;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_SKY_BLUE = 0xFF87CEFA;
	/**
	 * The color light_slate_gray with an RGB value of #778899
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#778899;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_SLATE_GRAY = 0xFF778899;
	/**
	 * The color light_slate_grey with an RGB value of #778899
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#778899;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_SLATE_GREY = 0xFF778899;
	/**
	 * The color light_steel_blue with an RGB value of #B0C4DE
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#B0C4DE;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_STEEL_BLUE = 0xFFB0C4DE;
	/**
	 * The color light_yellow with an RGB value of #FFFFE0
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFFE0;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIGHT_YELLOW = 0xFFFFFFE0;
	/**
	 * The color lime with an RGB value of #00FF00
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#00FF00;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIME = 0xFF00FF00;
	/**
	 * The color lime_green with an RGB value of #32CD32
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#32CD32;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LIME_GREEN = 0xFF32CD32;
	/**
	 * The color linen with an RGB value of #FAF0E6
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FAF0E6;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int LINEN = 0xFFFAF0E6;
	/**
	 * The color magenta with an RGB value of #FF00FF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF00FF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MAGENTA = 0xFFFF00FF;
	/**
	 * The color maroon with an RGB value of #800000
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#800000;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MAROON = 0xFF800000;
	/**
	 * The color medium_aquamarine with an RGB value of #66CDAA
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#66CDAA;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MEDIUM_AQUAMARINE = 0xFF66CDAA;
	/**
	 * The color medium_blue with an RGB value of #0000CD
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#0000CD;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MEDIUM_BLUE = 0xFF0000CD;
	/**
	 * The color medium_orchid with an RGB value of #BA55D3
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#BA55D3;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MEDIUM_ORCHID = 0xFFBA55D3;
	/**
	 * The color medium_purple with an RGB value of #9370DB
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#9370DB;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MEDIUM_PURPLE = 0xFF9370DB;
	/**
	 * The color medium_sea_green with an RGB value of #3CB371
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#3CB371;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MEDIUM_SEA_GREEN = 0xFF3CB371;
	/**
	 * The color medium_slate_blue with an RGB value of #7B68EE
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#7B68EE;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MEDIUM_SLATE_BLUE = 0xFF7B68EE;
	/**
	 * The color medium_spring_green with an RGB value of #00FA9A
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#00FA9A;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MEDIUM_SPRING_GREEN = 0xFF00FA9A;
	/**
	 * The color medium_turquoise with an RGB value of #48D1CC
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#48D1CC;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MEDIUM_TURQUOISE = 0xFF48D1CC;
	/**
	 * The color medium_violet_red with an RGB value of #C71585
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#C71585;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MEDIUM_VIOLET_RED = 0xFFC71585;
	/**
	 * The color midnight_blue with an RGB value of #191970
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#191970;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MIDNIGHT_BLUE = 0xFF191970;
	/**
	 * The color mint_cream with an RGB value of #F5FFFA
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F5FFFA;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MINT_CREAM = 0xFFF5FFFA;
	/**
	 * The color misty_rose with an RGB value of #FFE4E1
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFE4E1;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MISTY_ROSE = 0xFFFFE4E1;
	/**
	 * The color moccasin with an RGB value of #FFE4B5
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFE4B5;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int MOCCASIN = 0xFFFFE4B5;
	/**
	 * The color navajo_white with an RGB value of #FFDEAD
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFDEAD;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int NAVAJO_WHITE = 0xFFFFDEAD;
	/**
	 * The color navy with an RGB value of #000080
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#000080;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int NAVY = 0xFF000080;
	/**
	 * The color old_lace with an RGB value of #FDF5E6
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FDF5E6;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int OLD_LACE = 0xFFFDF5E6;
	/**
	 * The color olive with an RGB value of #808000
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#808000;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int OLIVE = 0xFF808000;
	/**
	 * The color olive_drab with an RGB value of #6B8E23
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#6B8E23;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int OLIVE_DRAB = 0xFF6B8E23;
	/**
	 * The color orange with an RGB value of #FFA500
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFA500;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int ORANGE = 0xFFFFA500;
	/**
	 * The color orange_red with an RGB value of #FF4500
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF4500;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int ORANGE_RED = 0xFFFF4500;
	/**
	 * The color orchid with an RGB value of #DA70D6
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#DA70D6;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int ORCHID = 0xFFDA70D6;
	/**
	 * The color pale_goldenrod with an RGB value of #EEE8AA
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#EEE8AA;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int PALE_GOLDENROD = 0xFFEEE8AA;
	/**
	 * The color pale_green with an RGB value of #98FB98
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#98FB98;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int PALE_GREEN = 0xFF98FB98;
	/**
	 * The color pale_turquoise with an RGB value of #AFEEEE
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#AFEEEE;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int PALE_TURQUOISE = 0xFFAFEEEE;
	/**
	 * The color pale_violet_red with an RGB value of #DB7093
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#DB7093;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int PALE_VIOLET_RED = 0xFFDB7093;
	/**
	 * The color papaya_whip with an RGB value of #FFEFD5
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFEFD5;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int PAPAYA_WHIP = 0xFFFFEFD5;
	/**
	 * The color peach_puff with an RGB value of #FFDAB9
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFDAB9;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int PEACH_PUFF = 0xFFFFDAB9;
	/**
	 * The color peru with an RGB value of #CD853F
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#CD853F;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int PERU = 0xFFCD853F;
	/**
	 * The color pink with an RGB value of #FFC0CB
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFC0CB;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int PINK = 0xFFFFC0CB;
	/**
	 * The color plum with an RGB value of #DDA0DD
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#DDA0DD;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int PLUM = 0xFFDDA0DD;
	/**
	 * The color powder_blue with an RGB value of #B0E0E6
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#B0E0E6;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int POWDER_BLUE = 0xFFB0E0E6;
	/**
	 * The color purple with an RGB value of #800080
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#800080;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int PURPLE = 0xFF800080;
	/**
	 * The color red with an RGB value of #FF0000
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF0000;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int RED = 0xFFFF0000;
	/**
	 * The color rosy_brown with an RGB value of #BC8F8F
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#BC8F8F;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int ROSY_BROWN = 0xFFBC8F8F;
	/**
	 * The color royal_blue with an RGB value of #4169E1
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#4169E1;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int ROYAL_BLUE = 0xFF4169E1;
	/**
	 * The color saddle_brown with an RGB value of #8B4513
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#8B4513;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SADDLE_BROWN = 0xFF8B4513;
	/**
	 * The color salmon with an RGB value of #FA8072
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FA8072;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SALMON = 0xFFFA8072;
	/**
	 * The color sandy_brown with an RGB value of #F4A460
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F4A460;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SANDY_BROWN = 0xFFF4A460;
	/**
	 * The color sea_green with an RGB value of #2E8B57
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#2E8B57;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SEA_GREEN = 0xFF2E8B57;
	/**
	 * The color sea_shell with an RGB value of #FFF5EE
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFF5EE;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SEA_SHELL = 0xFFFFF5EE;
	/**
	 * The color sienna with an RGB value of #A0522D
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#A0522D;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SIENNA = 0xFFA0522D;
	/**
	 * The color silver with an RGB value of #C0C0C0
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#C0C0C0;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SILVER = 0xFFC0C0C0;
	/**
	 * The color sky_blue with an RGB value of #87CEEB
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#87CEEB;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SKY_BLUE = 0xFF87CEEB;
	/**
	 * The color slate_blue with an RGB value of #6A5ACD
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#6A5ACD;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SLATE_BLUE = 0xFF6A5ACD;
	/**
	 * The color slate_gray with an RGB value of #708090
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#708090;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SLATE_GRAY = 0xFF708090;
	/**
	 * The color slate_grey with an RGB value of #708090
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#708090;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SLATE_GREY = 0xFF708090;
	/**
	 * The color snow with an RGB value of #FFFAFA
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFAFA;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SNOW = 0xFFFFFAFA;
	/**
	 * The color spring_green with an RGB value of #00FF7F
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#00FF7F;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int SPRING_GREEN = 0xFF00FF7F;
	/**
	 * The color steel_blue with an RGB value of #4682B4
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#4682B4;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int STEEL_BLUE = 0xFF4682B4;
	/**
	 * The color tan with an RGB value of #D2B48C
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#D2B48C;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int TAN = 0xFFD2B48C;
	/**
	 * The color teal with an RGB value of #008080
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#008080;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int TEAL = 0xFF008080;
	/**
	 * The color thistle with an RGB value of #D8BFD8
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#D8BFD8;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int THISTLE = 0xFFD8BFD8;
	/**
	 * The color tomato with an RGB value of #FF6347
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF6347;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int TOMATO = 0xFFFF6347;
	/**
	 * A fully transparent color with an ARGB value of #00000000
	 */
	public static final int TRANSPARENT = 0x00000000;
	/**
	 * The color turquoise with an RGB value of #40E0D0
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#40E0D0;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int TURQUOISE = 0xFF40E0D0;
	/**
	 * The color violet with an RGB value of #EE82EE
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#EE82EE;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int VIOLET = 0xFFEE82EE;
	/**
	 * The color wheat with an RGB value of #F5DEB3
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F5DEB3;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int WHEAT = 0xFFF5DEB3;
	/**
	 * The color white with an RGB value of #FFFFFF
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFFFF;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int WHITE = 0xFFFFFFFF;
	/**
	 * The color white_smoke with an RGB value of #F5F5F5
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#F5F5F5;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int WHITE_SMOKE = 0xFFF5F5F5;
	/**
	 * The color yellow with an RGB value of #FFFF00
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFF00;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int YELLOW = 0xFFFFFF00;
	/**
	 * The color yellow_green with an RGB value of #9ACD32
	 * <div style="border:1px solid black;width:40px;height:20px;background-color:#9ACD32;float:right;margin: 0 10px 0 0"></div>
	 */
	public static final int YELLOW_GREEN = 0xFF9ACD32;

	public static int getByToken(String token) {
		try {
			Field[] fields = Colors.class.getFields();
			return fields[token.hashCode() % fields.length].getInt(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
