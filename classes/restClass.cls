public class restClass {
    @auraEnabled
    public static pagess makeCall(){
        Http http = new Http();
        HttpRequest req = new HttpRequest();
        /* https://demo1.fusionitas.com/webpro/services/page/1.0?productId=10177&action=retrievePages*/
        req.setEndpoint('https://demo1.fusionitas.com/webpro/services/page/1.0?productId=10177&action=retrievePages');
        req.setMethod('POST');
        req.setHeader('Content-Type', 'application/json');
        
        
        //  req.setHeader('companyName', 'FusionSystemsInc');
        
        req.setHeader('clientId', 'cLkBbGk4F6V+8gz2TWjweQ==YP+UDEN67YjR6USNWHUQFPxL/TzJSOy/vRGJTlw0xSI=');        
        req.setHeader('clientToken', '9VajXbjOml8mSCl0YWxzFg==NpTeytekJZDQ4LFi/IZhiD/a+9t6UsDH/ZmWMAK9OB2Sxear51dPWGwez0KrhEYl+t/WUHdgaqVWDIV3iUqIB7X2b39VpfyU3jy5GP8+/AjZDQ0wUooUVuqL5IgvR3s7');
        //  req.setHeader('token', '9VajXbjOml8mSCl0YWxzFg==NpTeytekJZDQ4LFi/IZhiD/a+9t6UsDH/ZmWMAK9OB2Sxear51dPWGwez0KrhEYl+t/WUHdgaqVWDIV3iUqIB7X2b39VpfyU3jy5GP8+/AjZDQ0wUooUVuqL5IgvR3s7');
        // req.setHeader('appUser', UserInfo.getUserName());
        req.setTimeout(60000); // 60 seconds timeout
        
        HttpResponse response = http.send(req);
        System.debug(response.getBody());
       // pagess m = (pagess) JSON.deserializeUntyped(response.getBody());
        pagess m = (pagess)JSON.deserialize(response.getBody(), pagess.class);
        return m;
    }
    
    public class pages{
        @auraEnabled public  string pageId;
        @auraEnabled public  string pageName;
    }
    public class pagess{
         @auraEnabled public list<pages> pageNames;
    }
}