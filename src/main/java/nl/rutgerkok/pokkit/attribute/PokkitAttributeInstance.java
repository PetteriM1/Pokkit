package nl.rutgerkok.pokkit.attribute;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import nl.rutgerkok.pokkit.Pokkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PokkitAttributeInstance implements AttributeInstance {

    private static final BiMap<Attribute, Integer> map = HashBiMap.create(12);

    static {
        map.put(Attribute.GENERIC_MAX_HEALTH, cn.nukkit.entity.Attribute.MAX_HEALTH);
        map.put(Attribute.GENERIC_FOLLOW_RANGE, cn.nukkit.entity.Attribute.FOLLOW_RANGE);
        map.put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, cn.nukkit.entity.Attribute.KNOCKBACK_RESISTANCE);
        map.put(Attribute.GENERIC_MOVEMENT_SPEED, cn.nukkit.entity.Attribute.MOVEMENT_SPEED);
//        map.put(Attribute.GENERIC_FLYING_SPEED, cn.nukkit.entity.Attribute.);
        map.put(Attribute.GENERIC_ATTACK_DAMAGE, cn.nukkit.entity.Attribute.ATTACK_DAMAGE);
//        map.put(Attribute.GENERIC_ATTACK_SPEED, cn.nukkit.entity.Attribute.);
//        map.put(Attribute.GENERIC_ARMOR, cn.nukkit.entity.Attribute.);
//        map.put(Attribute.GENERIC_ARMOR_TOUGHNESS, cn.nukkit.entity.Attribute.);
//        map.put(Attribute.GENERIC_LUCK, cn.nukkit.entity.Attribute.LUCK);
//        map.put(Attribute.HORSE_JUMP_STRENGTH, cn.nukkit.entity.Attribute.);
//        map.put(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS, cn.nukkit.entity.Attribute.);
    }

    private Attribute bukkit;
    private cn.nukkit.entity.Attribute nukkit;

    private List<AttributeModifier> modifiers = new ArrayList<>();

    public PokkitAttributeInstance(cn.nukkit.entity.Attribute nukkit) {
        this(fromNukkit(nukkit.getId()), nukkit);
    }

    public PokkitAttributeInstance(Attribute bukkit, cn.nukkit.entity.Attribute nukkit) {
        this.bukkit = bukkit;
        this.nukkit = nukkit;
    }

    @Override
    public Attribute getAttribute() {
        return bukkit;
    }

    @Override
    public double getBaseValue() {
        return nukkit.getValue();
    }

    @Override
    public void setBaseValue(double v) {
        this.nukkit.setValue((float) v);
    }

    @Override
    public Collection<AttributeModifier> getModifiers() {
        return new ArrayList<>(modifiers);
    }

    @Override
    public void addModifier(AttributeModifier attributeModifier) {
        modifiers.add(attributeModifier);
    }

    @Override
    public void removeModifier(AttributeModifier attributeModifier) {
        modifiers.remove(attributeModifier);
    }

    @Override
    public double getValue() {
        return nukkit.getValue();
    }

    @Override
    public double getDefaultValue() {
        return nukkit.getDefaultValue();
    }

    public static int fromBukkit(Attribute bukkit) {
        Integer nukkit = map.get(bukkit);

        if (nukkit == null) {
            Pokkit.unsupported();
        }

        return nukkit;
    }

    public static Attribute fromNukkit(int nukkit) {
        Attribute bukkit = map.inverse().get(nukkit);
        if (bukkit == null) {
            Pokkit.unsupported();
        }

        return bukkit;
    }
}
