package online.omnia.comparison;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by lollipop on 11.07.2017.
 */
public class PostbackHandler {

    public Map<String, String> getPostbackParameters(String url){

        Map<String, String> parametersMap = new HashMap<>();
        if (url == null || url.isEmpty()) return parametersMap;

        String[] urlParts = url.split("\\?");

        if (urlParts.length != 2){
            System.out.println("No ?");
            System.out.println(Arrays.asList(urlParts));
            return parametersMap;
        }

        String parameters = urlParts[1];
        if (!parameters.contains("&")) {
            System.out.println("Not found &");
            String[] pair = parameters.split("=");
            if (pair.length == 0) return parametersMap;
            if (pair.length == 2) {
                parametersMap.put(pair[0], pair[1]);
            }
            else if (pair.length == 1) {
                parametersMap.put(pair[0], "");
            }
            return parametersMap;
        }
        String[] keyValuePairs = parameters.split("&");
        String[] pairs;

        for (String keyValuePair : keyValuePairs) {
            pairs = keyValuePair.split("=");
            if (pairs.length == 2) {
                parametersMap.put(pairs[0], pairs[1]);
            }
            else if (pairs.length == 1) {
                parametersMap.put(pairs[0], "");
            }
        }
        System.out.println("Parameters have been got");
        return parametersMap;
    }
    public PostBackEntity fillPostback(Map<String, String> parameters) {
        PostBackEntity postBackEntity = new PostBackEntity();

        String clickid = parameters.containsKey("clickid") ? parameters.get("clickid") : null;
        String prefix = null;
        int prefixNumber;
        int clickIdLength = clickid != null ? clickid.length() : 0;
        if (clickIdLength > 0 && clickid.contains("_")){
            prefixNumber = clickid.indexOf("_");
            prefix = clickid.substring(0, prefixNumber);
            if (!isPrefixInTrackers(prefix)) prefix = null;
            else clickid = clickid.substring(prefixNumber + 1, clickIdLength);
        }

        postBackEntity.setClickId(clickid);
        if (prefix == null) prefix = "333";
        postBackEntity.setPrefix(prefix);

        if (parameters.containsKey("sum") && parameters.get("sum").matches("(\\d+.\\d+)|(\\d+)")) postBackEntity.setSum(Double.parseDouble(parameters.get("sum")));
        if (parameters.containsKey("currency")) postBackEntity.setCurrency(parameters.get("currency"));
        else postBackEntity.setCurrency("USD");
        if (parameters.containsKey("goal")) postBackEntity.setGoal(parameters.get("goal"));
        if (parameters.containsKey("status")) postBackEntity.setStatus(parameters.get("status"));
        if (parameters.containsKey("advname")) postBackEntity.setAdvName(parameters.get("advname"));
        if (parameters.containsKey("offername")) postBackEntity.setOfferName(parameters.get("offername"));
        if (parameters.containsKey("transactionid")) postBackEntity.setTransactionId(parameters.get("transactionid"));
        if (parameters.containsKey("idfa")) postBackEntity.setIDFA(parameters.get("idfa"));
        if (parameters.containsKey("gaid")) postBackEntity.setGaId("gaid");
        if (parameters.containsKey("ip")) postBackEntity.setIpAddress(parameters.get("ip"));
        if (parameters.containsKey("secret")) postBackEntity.setSecretKey(parameters.get("secret"));
        if (parameters.containsKey("t1")) postBackEntity.setT1(parameters.get("t1"));
        if (parameters.containsKey("t2")) postBackEntity.setT2(parameters.get("t2"));
        if (parameters.containsKey("t3")) postBackEntity.setT3(parameters.get("t3"));
        if (parameters.containsKey("t4")) postBackEntity.setT4(parameters.get("t4"));
        if (parameters.containsKey("t5")) postBackEntity.setT5(parameters.get("t5"));
        if (parameters.containsKey("t6")) postBackEntity.setT6(parameters.get("t6"));
        if (parameters.containsKey("t7")) postBackEntity.setT7(parameters.get("t7"));
        if (parameters.containsKey("t8")) postBackEntity.setT8(parameters.get("t8"));
        if (parameters.containsKey("t9")) postBackEntity.setT9(parameters.get("t9"));
        if (parameters.containsKey("t10")) postBackEntity.setT10(parameters.get("t10"));
        if (parameters.containsKey("offerid")) postBackEntity.setOfferId(parameters.get("offerid"));
        if (parameters.containsKey("afid") && parameters.get("afid").matches("\\d+")) postBackEntity.setAfid(Integer.parseInt(parameters.get("afid")));
        if (parameters.containsKey("postbacksend") && parameters.get("postbacksend").matches("\\d+")) postBackEntity.setPostbackSend(Integer.parseInt(parameters.get("postback_send")));
        else postBackEntity.setPostbackSend(2);
        postBackEntity.setDuplicate("original");

        Random random = new Random();
        StringBuilder actionId = new StringBuilder();
        int numberLength = random.nextInt(8) + 12;
        while (true) {
            for (int i = 0; i < numberLength; i++) {
                actionId.append(random.nextInt(10));
            }
            if (!isTransactionidInDB(actionId.toString())) break;
            else actionId = new StringBuilder();
        }
        postBackEntity.setActionId(actionId.toString());

        if (postBackEntity.getTransactionId() != null
                && isPostbackPartial(postBackEntity.getClickId())){
            System.out.println("is postback PARTIAl");
            postBackEntity.setDuplicate("PARTIAL");
        }

        return postBackEntity;
    }

    public ErrorPostBackEntity createError(PostBackEntity postBackEntity) {
        ErrorPostBackEntity errorPostBackEntity = new ErrorPostBackEntity();
        errorPostBackEntity.setActionId(postBackEntity.getActionId());
        errorPostBackEntity.setAdvName(postBackEntity.getAdvName());
        errorPostBackEntity.setAfid(postBackEntity.getAfid());
        errorPostBackEntity.setClickId(postBackEntity.getClickId());
        errorPostBackEntity.setCurrency(postBackEntity.getCurrency());
        errorPostBackEntity.setDate(postBackEntity.getDate());
        errorPostBackEntity.setDuplicate(postBackEntity.getDuplicate());
        errorPostBackEntity.setFullURL(postBackEntity.getFullURL());
        errorPostBackEntity.setGaId(postBackEntity.getGaId());
        errorPostBackEntity.setGoal(postBackEntity.getGoal());
        errorPostBackEntity.setId(postBackEntity.getId());
        errorPostBackEntity.setIDFA(postBackEntity.getIDFA());
        errorPostBackEntity.setIpAddress(postBackEntity.getIpAddress());
        errorPostBackEntity.setOfferId(postBackEntity.getOfferId());
        errorPostBackEntity.setOfferName(postBackEntity.getOfferName());
        errorPostBackEntity.setPostbackSend(postBackEntity.getPostbackSend());
        errorPostBackEntity.setPrefix(postBackEntity.getPrefix());
        errorPostBackEntity.setSecretKey(postBackEntity.getSecretKey());
        errorPostBackEntity.setStatus(postBackEntity.getStatus());
        errorPostBackEntity.setSum(postBackEntity.getSum());
        errorPostBackEntity.setT1(postBackEntity.getT1());
        errorPostBackEntity.setT2(postBackEntity.getT2());
        errorPostBackEntity.setT3(postBackEntity.getT3());
        errorPostBackEntity.setT4(postBackEntity.getT4());
        errorPostBackEntity.setT5(postBackEntity.getT5());
        errorPostBackEntity.setT6(postBackEntity.getT6());
        errorPostBackEntity.setT7(postBackEntity.getT7());
        errorPostBackEntity.setT8(postBackEntity.getT8());
        errorPostBackEntity.setT9(postBackEntity.getT9());
        errorPostBackEntity.setT10(postBackEntity.getT10());
        errorPostBackEntity.setTime(postBackEntity.getTime());
        errorPostBackEntity.setDuplicate(postBackEntity.getDuplicate());
        errorPostBackEntity.setTransactionId(postBackEntity.getTransactionId());

        return errorPostBackEntity;
    }

    private boolean isPostbackPartial(String clickId) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        return mySQLDao.isClickidInDb(clickId);
    }

    private boolean isTransactionidInDB(String transactionId) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        AbstractPostBackEntity postBackEntity = mySQLDao.getPostbackByTransactionId(transactionId);
        return postBackEntity != null;
    }

    private boolean isPrefixInTrackers(String prefix) {
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        TrackerEntity trackerEntity = mySQLDao.getTrackerByPrefix(prefix);
        return trackerEntity != null;
    }
}
