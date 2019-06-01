package com.example.glassespart.config;

public class NetworkConfig {
// DEFAULT CONFIG
    private static String DEFAULTLOCALIPADDRESS = "192.168.43.109";
    private static int DEFAULTLOCALPORT = 3334;

    private static String DEFAULTTARGETIPADDRESS = "192.168.43.154";
    private static int DEFAULTTARGETPORT = 3333;

// CURRENT CONFIG
    public static String LOCALIPADDRESS = DEFAULTLOCALIPADDRESS;
    public static int LOCALPORT = DEFAULTLOCALPORT;

    public static String TARGETIPADDRESS = DEFAULTTARGETIPADDRESS;
    public static int TARGETPORT = DEFAULTTARGETPORT;

//MOVE CURRENT TO DEFAULT
    public static void toDefaultConfig(){
        LOCALIPADDRESS = DEFAULTLOCALIPADDRESS;
        LOCALPORT = DEFAULTLOCALPORT;

        TARGETIPADDRESS = DEFAULTTARGETIPADDRESS;
        TARGETPORT = DEFAULTTARGETPORT;
    }
}
