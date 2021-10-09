package fr.whiizyyy.insaneac;

import java.io.IOException;

import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class License {
	
	private String licenseKey;
	private Plugin plugin;
	private String validationServer;
	private String securityKey = "YecoF0I6M05thxLeokoHuW8iUhTdIUInjkfF";
	private boolean debug = false;
	
	public License(String licenseKey, String validationServer, Plugin plugin){
		this.licenseKey = licenseKey;
		this.plugin = plugin;
		this.validationServer = validationServer;
	}
	
	public License setSecurityKey(String securityKey){
		this.securityKey = securityKey;
		return this;
	}
	
	public License debug(){
		debug = true;
		return this;
	}
	
	public boolean register(){
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "§8==========§r §8[§cInsaneAC§8]§r §8=========="));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "§8Connecting to §cLicense Server..."));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "§8Checking your §cLicense..."));
		ValidationType vt = isValid();
		if(vt == ValidationType.VALID){
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "§cLicense §2valid!"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "§cEnabling InsaneCore By Whiizyyy!"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "§8==========§r §8[§cInsaneAC§8]§r §8=========="));
			return true;
		}else{
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "§cLicense is NOT valid!"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "§cDisabling InsaneAC By Whiizyyy!"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "§cContact Whiizyyy#0681"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "§8==========§r §8[§cInsaneAC§8]§r §8=========="));
			
			
			Bukkit.getScheduler().cancelTasks(plugin);
			Bukkit.getPluginManager().disablePlugin(plugin);
			return false;
		}
	}
	
	public boolean isValidSimple(){
		return (isValid() == ValidationType.VALID);
	}
	
	public ValidationType isValid(){
		String rand = toBinary(UUID.randomUUID().toString());
		String sKey = toBinary(securityKey);
		String key  = toBinary(licenseKey);
		
		try{
			URL url = new URL(validationServer+"?v1="+xor(rand, sKey)+"&v2="+xor(rand, key)+"&pl="+plugin.getName());
			if(debug) System.out.println("RequestURL -> "+url.toString());
			Scanner s = new Scanner(url.openStream());
			if(s.hasNext()){
				String response = s.next();
				s.close();
				try{
					return ValidationType.valueOf(response);
				}catch(IllegalArgumentException exc){
					String respRand = xor(xor(response, key), sKey);
					if(rand.substring(0, respRand.length()).equals(respRand)) return ValidationType.VALID;
					else return ValidationType.WRONG_RESPONSE;
				}
			}else{
				s.close();
				return ValidationType.PAGE_ERROR;
			}
		}catch(IOException exc){ 
			if(debug) exc.printStackTrace();
			return ValidationType.URL_ERROR;
		}
	}
	
	
	//
	// Cryptographic
	//
	
	private static String xor(String s1, String s2){
		String s0 = "";
		for(int i = 0; i < (s1.length() < s2.length() ? s1.length() : s2.length()) ; i++) s0 += Byte.valueOf(""+s1.charAt(i))^Byte.valueOf(""+s2.charAt(i));
		return s0;
	}
	
	//
	// Enums
	//
	
	public enum LogType{
		NORMAL, LOW, NONE;
	}
	
	public static enum ValidationType{
		WRONG_RESPONSE, PAGE_ERROR, URL_ERROR, KEY_OUTDATED, KEY_NOT_FOUND, NOT_VALID_IP, INVALID_PLUGIN, VALID;
	}
	
	//
	// Binary methods
	//
	
	private String toBinary(String s){
		byte[] bytes = s.getBytes();
		  StringBuilder binary = new StringBuilder();
		  for (byte b : bytes)
		  {
		     int val = b;
		     for (int i = 0; i < 8; i++)
		     {
		        binary.append((val & 128) == 0 ? 0 : 1);
		        val <<= 1;
		     }
		  }
		  return binary.toString();
	}
	
}
