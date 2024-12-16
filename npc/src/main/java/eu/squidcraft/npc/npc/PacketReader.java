package eu.squidcraft.npc.npc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import eu.squidcraft.npc.NPCPlugin;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_16_R3.PacketPlayInUseEntity;

public class PacketReader {
	
	Channel channel;
	public static Map<UUID, Channel> channels = new HashMap<>();
	
	public void inject(Player player) {
		
		CraftPlayer nmsPlayer = (CraftPlayer) player;
		channel = nmsPlayer.getHandle().playerConnection.a().channel;
		channels.put(player.getUniqueId(), channel);
		
		if (channel.pipeline().get("PacketInjector") != null) return;
		
		channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {

			@Override
			protected void decode(ChannelHandlerContext handler, PacketPlayInUseEntity packet, List<Object> list) throws Exception {
				
				list.add(packet);
				read(player, packet);
				
			}
		});
	}
	public void uninject(Player player) {
		
		if (channels.size() == 0 || channels.isEmpty())return;
		channel = channels.get(player.getUniqueId());
		if (channel.pipeline().get("PacketInjector") != null) {
			channel.pipeline().remove("PacketInjector");
		}
		
	}
	
	private void read(Player player, PacketPlayInUseEntity packet) {
		
		if (getValue(packet, "action").toString().equalsIgnoreCase("ATTACK"))return;
		if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT_AT"))return;
		
		int id = (int) getValue(packet, "a");
		
		if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")) {

			for (UUID uuids : NPC.getNpcs().keySet()) {
				NoPlayerCharacter npc = NPC.getNpcs().get(uuids);
				
				if (npc.getNpc().getId() == id) {
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(NPCPlugin.getPlugin(NPCPlugin.class), new Runnable() {
						
						@Override
						public void run() {
							Bukkit.getPluginManager().callEvent(new PlayerInteractAtNPCEvent(npc, player, uuids));
							
						}
					}, 0);
				}
			}
		}
		
	}
	
	private Object getValue(Object instance, String name) {
		
		Object result = null;
		
		try {
			
			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			result = field.get(instance);
			field.setAccessible(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}


















