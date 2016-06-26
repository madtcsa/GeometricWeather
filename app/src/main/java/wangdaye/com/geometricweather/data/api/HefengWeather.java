package wangdaye.com.geometricweather.data.api;

import com.google.gson.Gson;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import wangdaye.com.geometricweather.data.model.HefengResult;
import wangdaye.com.geometricweather.data.model.Location;

/**
 * Hefeng weather.
 * */

public class HefengWeather {
    // data
    private static final String httpUrl = "http://apis.baidu.com/heweather/weather/free";
    private static final String httpArg = "city=";
    private static final String APP_KEY_DAILY = "f8f22c028b3ad53163da5a7a0ca854b3";
    private static final String APP_KEY_HOURLY = "9c66dd2f8347b7b7b69d4521051a5eb5";

    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;

    /** <br> weather. */

    public static HefengResult getWeather(String location, boolean isDaily) {
        BufferedReader reader;
        String result;
        String requestCode = httpUrl + "?" + httpArg + buildRequestLocation(location);
        StringBuilder sbf = new StringBuilder();
        HefengResult hefengResult = null;
        try {
            URL url = new URL(requestCode);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  isDaily ? APP_KEY_DAILY : APP_KEY_HOURLY);
            connection.setConnectTimeout(DEF_CONN_TIMEOUT);
            connection.setReadTimeout(DEF_READ_TIMEOUT);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
            String resultExchange = result.replaceFirst("HeWeather data service 3.0", "heWeather");
            result = resultExchange.replaceAll("now\":\\{\"cond", "now\":{\"condNow");
            hefengResult = new Gson().fromJson(result, HefengResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hefengResult;
    }

    public static String charToPinyin(String location) {
        switch (location) {
            case "厦门":
                return "xiamen";

            case "蚌埠":
                return "bengbu";

            case "浚县":
                return "xunxian";

            case "泌阳":
                return "biyang";

            case "洪洞":
                return "hongtong";

            case "六安":
                return "luan";

            case "黄陂":
                return "huangbi";

            case "番禺":
                return "panyu";

            case "香港":
                return "hongkang";

            case "台北":
                return "taipei";

            case "澳门":
                return "macao";

            default:
                break;
        }

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE); // 小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 不带声调
        format.setVCharType(HanyuPinyinVCharType.WITH_V); // v

        char[] input = location.trim().toCharArray();
        StringBuilder output = new StringBuilder("");

        try {
            for (char anInput : input) {
                if (Character.toString(anInput).matches("[\u4E00-\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(anInput, format);
                    output.append(temp[0]);
                    output.append(" ");
                } else
                    output.append(Character.toString(anInput));
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public static String getWeatherKind(String weatherCode) {
        int code = Integer.parseInt(weatherCode);
        if (code == 100) {
            return "晴";
        } else if (100 < code && code < 104) {
            return "云";
        } else if (code == 104) {
            return "阴";
        } else if (199 < code && code < 214) {
            return "风";
        } else if (299 < code && code < 302) {
            return "雨";
        } else if (301 < code && code < 304) {
            return "雷雨";
        } else if (code == 304) {
            return "雹";
        } else if (304< code && code < 313) {
            return "雨";
        } else if (code == 313) {
            return "雨夹雪";
        } else if (399 < code && code < 404) {
            return "雪";
        } else if (403 < code && code < 407) {
            return "雨夹雪";
        } else if (code == 407) {
            return "雪";
        } else if (499 < code && code < 502) {
            return "雾";
        } else if (501 < code && code < 509) {
            return "霾";
        } else if (code == 900) {
            return "晴";
        } else if (code == 901) {
            return "雪";
        } else {
            return "阴";
        }
    }

    public static int getLatestDataPosition(HefengResult result) {
        if (result == null || result.heWeather.size() == 0 || !result.heWeather.get(0).status.equals("ok")) {
                return 0;
        }
        int position = 0;
        String updateTime = result.heWeather.get(0).basic.update.loc;
        for (int i = 1; i < result.heWeather.size(); i ++) {
            if (result.heWeather.get(i).basic.update.loc.compareTo(updateTime) > 0) {
                position = i;
                updateTime = result.heWeather.get(i).basic.update.loc;
            }
        }
        return position;
    }

    private static String buildRequestLocation(String location) {
        if (Location.isEngLocation(location)) {
            while (true) {
                if (location.charAt(0) == ' ') {
                    location = location.replaceFirst(" ", "");
                } else {
                    break;
                }
            }

            for (int i = 1; i < location.length() - 1; i ++) {
                if (location.charAt(i) == ' ' &&
                        (location.charAt(i - 1) == ' ' || location.charAt(i + 1) == ' ')) {
                    location = location.replaceFirst(" ", "");
                    i --;
                } else if (location.charAt(i) == ' ' && location.charAt(i - 1) != ' ' && location.charAt(i + 1) != ' ') {
                    location = location.replaceFirst(" ", "+");
                }
            }

            return location.replaceAll(" ", "");
        } else {
            return charToPinyin(location.replace(" ", "")).replaceAll(" ", "");
        }
    }
}