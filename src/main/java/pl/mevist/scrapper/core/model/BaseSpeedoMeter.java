package pl.mevist.scrapper.core.model;


import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO:
//  This could be usefull later, for know its bit overcompliacting things. Just take simple String and save it.
public class BaseSpeedoMeter {
    private float kw;
    private float hp;

    public static final float KW_TO_HP_CONST = 1.341f;
    public static final float HP_TO_KW_CONST = 0.7457f;

    public BaseSpeedoMeter fromHp(float hp) {
        this.hp = hp;
        this.kw =  hp * KW_TO_HP_CONST;
        return this;
    }

    public BaseSpeedoMeter fromKw(float kw) {
        this.kw = kw;
        this.hp = kw * HP_TO_KW_CONST;
        return this;
    }

    // TODO:
    //  sanitizng speedometer may be useful later, for now save it as raw string
    private void sanitizeSpeedoMeter(String speedoMeter) {
        String input = "51 kW (69 KM)";
        input = input.replaceAll("[()]", "");

        Pattern pairPattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*([a-zA-Z]+)");
        Matcher matcher = pairPattern.matcher(input);

        List<SimpleEntry<Float, String>> values = new ArrayList<>();
        while (matcher.find()) {
            float value = Float.parseFloat(matcher.group(1));
            String unit = matcher.group(3).toLowerCase();
            values.add(new SimpleEntry<>(value, unit));
        }
    }
}
