package com.example.module_ad.bean;

import com.example.module_ad.base.AdActionBean;
import com.example.module_ad.base.AdTypeBean;

/**
 * @author wujinming QQ:1245074510
 * @name Wifi_Manager
 * @class name：com.example.module_ad.bean
 * @class describe
 * @time 2021/2/5 14:37:30
 * @class describe
 */
public class AdBean {
    /**
     * code : 0
     * message : 成功
     * data : {"start_page":{"spread_screen":{"status":true,"ad_origin":"gdt_toutiao","times":1,"change_times":300,"ad_percent":"0_100"}},"home_page":{"native_screen":{"status":true,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}},"setting_page":{"native_screen":{"status":true,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}},"exit_page":{"native_screen":{"status":true,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}},"Advertisement":{"kTouTiaoAppKey":"5156601","kTouTiaoKaiPing":"887454857","kTouTiaoBannerKey":"945969547","kTouTiaoChaPingKey":"945969548","kTouTiaoJiLiKey":"945969549","kTouTiaoSeniorKey":"945969531","kTouTiaoSmallSeniorKey":"","kGDTMobSDKAppKey":"1111587707","kGDTMobSDKChaPingKey":"9011777164894630","kGDTMobSDKKaiPingKey":"3071777184899515","kGDTMobSDKBannerKey":"2001175114899529","kGDTMobSDKNativeKey":"9071073174498596","kGDTMobSDKSmallNativeKey":"9021274154294527","kGDTMobSDKJiLiKey":"7081979124497508"}}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * start_page : {"spread_screen":{"status":true,"ad_origin":"gdt_toutiao","times":1,"change_times":300,"ad_percent":"0_100"}}
         * home_page : {"native_screen":{"status":true,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}}
         * setting_page : {"native_screen":{"status":true,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}}
         * exit_page : {"native_screen":{"status":true,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}}
         * Advertisement : {"kTouTiaoAppKey":"5156601","kTouTiaoKaiPing":"887454857","kTouTiaoBannerKey":"945969547","kTouTiaoChaPingKey":"945969548","kTouTiaoJiLiKey":"945969549","kTouTiaoSeniorKey":"945969531","kTouTiaoSmallSeniorKey":"","kGDTMobSDKAppKey":"1111587707","kGDTMobSDKChaPingKey":"9011777164894630","kGDTMobSDKKaiPingKey":"3071777184899515","kGDTMobSDKBannerKey":"2001175114899529","kGDTMobSDKNativeKey":"9071073174498596","kGDTMobSDKSmallNativeKey":"9021274154294527","kGDTMobSDKJiLiKey":"7081979124497508"}
         */

        private StartPageBean start_page;
        private HomePageBean home_page;
        private SettingPageBean setting_page;
        private ExitPageBean exit_page;
        private AdvertisementBean Advertisement;

        public StartPageBean getStart_page() {
            return start_page;
        }

        public void setStart_page(StartPageBean start_page) {
            this.start_page = start_page;
        }

        public HomePageBean getHome_page() {
            return home_page;
        }

        public void setHome_page(HomePageBean home_page) {
            this.home_page = home_page;
        }

        public SettingPageBean getSetting_page() {
            return setting_page;
        }

        public void setSetting_page(SettingPageBean setting_page) {
            this.setting_page = setting_page;
        }

        public ExitPageBean getExit_page() {
            return exit_page;
        }

        public void setExit_page(ExitPageBean exit_page) {
            this.exit_page = exit_page;
        }

        public AdvertisementBean getAdvertisement() {
            return Advertisement;
        }

        public void setAdvertisement(AdvertisementBean Advertisement) {
            this.Advertisement = Advertisement;
        }

        public static class StartPageBean extends AdTypeBean{
            /**
             * spread_screen : {"status":true,"ad_origin":"gdt_toutiao","times":1,"change_times":300,"ad_percent":"0_100"}
             */

            private SpreadScreenBean spread_screen;

            public SpreadScreenBean getSpread_screen() {
                return spread_screen;
            }

            public void setSpread_screen(SpreadScreenBean spread_screen) {
                this.spread_screen = spread_screen;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return spread_screen;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return spread_screen;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return spread_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return spread_screen;
            }

            public static class SpreadScreenBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 1
                 * change_times : 300
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }

                @Override
                public String getBaseAd_percent() {
                    return ad_percent;
                }

                @Override
                public boolean isBaseStatus() {
                    return status;
                }

                @Override
                public int getShowTime() {
                    return change_times;
                }
            }
        }

        public static class HomePageBean extends AdTypeBean {
            /**
             * native_screen : {"status":true,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeScreenBean native_screen;

            public NativeScreenBean getNative_screen() {
                return native_screen;
            }

            public void setNative_screen(NativeScreenBean native_screen) {
                this.native_screen = native_screen;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return native_screen;
            }

            public static class NativeScreenBean extends AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 5
                 * change_times : 10
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }

                @Override
                public String getBaseAd_percent() {
                    return ad_percent;
                }

                @Override
                public boolean isBaseStatus() {
                    return status;
                }

                @Override
                public int getShowTime() {
                    return change_times;
                }
            }
        }

        public static class SettingPageBean extends AdTypeBean {
            /**
             * native_screen : {"status":true,"ad_origin":"gdt_toutiao","times":300,"change_times":3,"ad_percent":"0_100"}
             */

            private NativeScreenBean native_screen;

            public NativeScreenBean getNative_screen() {
                return native_screen;
            }

            public void setNative_screen(NativeScreenBean native_screen) {
                this.native_screen = native_screen;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return native_screen;
            }

            public static class NativeScreenBean extends  AdActionBean{
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 300
                 * change_times : 3
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }

                @Override
                public String getBaseAd_percent() {
                    return ad_percent;
                }

                @Override
                public boolean isBaseStatus() {
                    return status;
                }

                @Override
                public int getShowTime() {
                    return change_times;
                }
            }
        }

        public static class ExitPageBean extends  AdTypeBean {
            /**
             * native_screen : {"status":true,"ad_origin":"gdt_toutiao","times":5,"change_times":10,"ad_percent":"0_100"}
             */

            private NativeScreenBean native_screen;

            public NativeScreenBean getNative_screen() {
                return native_screen;
            }

            public void setNative_screen(NativeScreenBean native_screen) {
                this.native_screen = native_screen;
            }

            @Override
            public AdActionBean getBaseBanner_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseNative_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseInsert_screen() {
                return native_screen;
            }

            @Override
            public AdActionBean getBaseIncentiveVideo_screen() {
                return native_screen;
            }

            public static class NativeScreenBean extends AdActionBean {
                /**
                 * status : true
                 * ad_origin : gdt_toutiao
                 * times : 5
                 * change_times : 10
                 * ad_percent : 0_100
                 */

                private boolean status;
                private String ad_origin;
                private int times;
                private int change_times;
                private String ad_percent;

                public boolean isStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public String getAd_origin() {
                    return ad_origin;
                }

                public void setAd_origin(String ad_origin) {
                    this.ad_origin = ad_origin;
                }

                public int getTimes() {
                    return times;
                }

                public void setTimes(int times) {
                    this.times = times;
                }

                public int getChange_times() {
                    return change_times;
                }

                public void setChange_times(int change_times) {
                    this.change_times = change_times;
                }

                public String getAd_percent() {
                    return ad_percent;
                }

                public void setAd_percent(String ad_percent) {
                    this.ad_percent = ad_percent;
                }

                @Override
                public String getBaseAd_percent() {
                    return ad_percent;
                }

                @Override
                public boolean isBaseStatus() {
                    return status;
                }

                @Override
                public int getShowTime() {
                    return change_times;
                }
            }
        }

        public static class AdvertisementBean {
            /**
             * kTouTiaoAppKey : 5156601
             * kTouTiaoKaiPing : 887454857
             * kTouTiaoBannerKey : 945969547
             * kTouTiaoChaPingKey : 945969548
             * kTouTiaoJiLiKey : 945969549
             * kTouTiaoSeniorKey : 945969531
             * kTouTiaoSmallSeniorKey :
             * kGDTMobSDKAppKey : 1111587707
             * kGDTMobSDKChaPingKey : 9011777164894630
             * kGDTMobSDKKaiPingKey : 3071777184899515
             * kGDTMobSDKBannerKey : 2001175114899529
             * kGDTMobSDKNativeKey : 9071073174498596
             * kGDTMobSDKSmallNativeKey : 9021274154294527
             * kGDTMobSDKJiLiKey : 7081979124497508
             */

            private String kTouTiaoAppKey;
            private String kTouTiaoKaiPing;
            private String kTouTiaoBannerKey;
            private String kTouTiaoChaPingKey;
            private String kTouTiaoJiLiKey;
            private String kTouTiaoSeniorKey;
            private String kTouTiaoSmallSeniorKey;
            private String kGDTMobSDKAppKey;
            private String kGDTMobSDKChaPingKey;
            private String kGDTMobSDKKaiPingKey;
            private String kGDTMobSDKBannerKey;
            private String kGDTMobSDKNativeKey;
            private String kGDTMobSDKSmallNativeKey;
            private String kGDTMobSDKJiLiKey;

            public String getKTouTiaoAppKey() {
                return kTouTiaoAppKey;
            }

            public void setKTouTiaoAppKey(String kTouTiaoAppKey) {
                this.kTouTiaoAppKey = kTouTiaoAppKey;
            }

            public String getKTouTiaoKaiPing() {
                return kTouTiaoKaiPing;
            }

            public void setKTouTiaoKaiPing(String kTouTiaoKaiPing) {
                this.kTouTiaoKaiPing = kTouTiaoKaiPing;
            }

            public String getKTouTiaoBannerKey() {
                return kTouTiaoBannerKey;
            }

            public void setKTouTiaoBannerKey(String kTouTiaoBannerKey) {
                this.kTouTiaoBannerKey = kTouTiaoBannerKey;
            }

            public String getKTouTiaoChaPingKey() {
                return kTouTiaoChaPingKey;
            }

            public void setKTouTiaoChaPingKey(String kTouTiaoChaPingKey) {
                this.kTouTiaoChaPingKey = kTouTiaoChaPingKey;
            }

            public String getKTouTiaoJiLiKey() {
                return kTouTiaoJiLiKey;
            }

            public void setKTouTiaoJiLiKey(String kTouTiaoJiLiKey) {
                this.kTouTiaoJiLiKey = kTouTiaoJiLiKey;
            }

            public String getKTouTiaoSeniorKey() {
                return kTouTiaoSeniorKey;
            }

            public void setKTouTiaoSeniorKey(String kTouTiaoSeniorKey) {
                this.kTouTiaoSeniorKey = kTouTiaoSeniorKey;
            }

            public String getKTouTiaoSmallSeniorKey() {
                return kTouTiaoSmallSeniorKey;
            }

            public void setKTouTiaoSmallSeniorKey(String kTouTiaoSmallSeniorKey) {
                this.kTouTiaoSmallSeniorKey = kTouTiaoSmallSeniorKey;
            }

            public String getKGDTMobSDKAppKey() {
                return kGDTMobSDKAppKey;
            }

            public void setKGDTMobSDKAppKey(String kGDTMobSDKAppKey) {
                this.kGDTMobSDKAppKey = kGDTMobSDKAppKey;
            }

            public String getKGDTMobSDKChaPingKey() {
                return kGDTMobSDKChaPingKey;
            }

            public void setKGDTMobSDKChaPingKey(String kGDTMobSDKChaPingKey) {
                this.kGDTMobSDKChaPingKey = kGDTMobSDKChaPingKey;
            }

            public String getKGDTMobSDKKaiPingKey() {
                return kGDTMobSDKKaiPingKey;
            }

            public void setKGDTMobSDKKaiPingKey(String kGDTMobSDKKaiPingKey) {
                this.kGDTMobSDKKaiPingKey = kGDTMobSDKKaiPingKey;
            }

            public String getKGDTMobSDKBannerKey() {
                return kGDTMobSDKBannerKey;
            }

            public void setKGDTMobSDKBannerKey(String kGDTMobSDKBannerKey) {
                this.kGDTMobSDKBannerKey = kGDTMobSDKBannerKey;
            }

            public String getKGDTMobSDKNativeKey() {
                return kGDTMobSDKNativeKey;
            }

            public void setKGDTMobSDKNativeKey(String kGDTMobSDKNativeKey) {
                this.kGDTMobSDKNativeKey = kGDTMobSDKNativeKey;
            }

            public String getKGDTMobSDKSmallNativeKey() {
                return kGDTMobSDKSmallNativeKey;
            }

            public void setKGDTMobSDKSmallNativeKey(String kGDTMobSDKSmallNativeKey) {
                this.kGDTMobSDKSmallNativeKey = kGDTMobSDKSmallNativeKey;
            }

            public String getKGDTMobSDKJiLiKey() {
                return kGDTMobSDKJiLiKey;
            }

            public void setKGDTMobSDKJiLiKey(String kGDTMobSDKJiLiKey) {
                this.kGDTMobSDKJiLiKey = kGDTMobSDKJiLiKey;
            }
        }
    }
}
