public with sharing class componentController {
    public static List<wrapPage> wrapPageList {get; set;}
    public static String txtBatchName{get;set;}
    public static String getBatchData{get;set;}
    public static String statusMessage{get;set;}
    public static boolean enableButton{get;set;}
    public static boolean isApexTest = false;
    
    
    
    
    @auraEnabled   
    public static List<wrapPage> retrievePages() {     //New api to display all the pages info
       /*  
        HttpRequest req = getHttpRequest('/webpro/services/page/1.0?productId=10177&action=retrievePages', 'retrievePages');
        
        Http http = new Http();
        if (!isApexTest){
           // String body = 'productId=10177&action=retrievePages';
           // req.setbody(body);
            
            HTTPResponse res = http.send(req);
            
            getBatchData = res.getBody();
        }
        else{
            getBatchData = '{\"pageNames\":[{\"pageId\":\"3403\",\"pageName\":\"Account\"}]}'; //replace 90 with 93 to show hard coded
            
        }
         */
        
        
     /*   getBatchData = '{\"pageNames\":[{\"pageId\":\"3403\",\"pageName\":\"Account\"}]}'; */
    /*     String json = getBatchData;
        Boolean result = json.contains('pageNames');
        if (!result)
            statusMessage = 'Error : ' + json;
        
        if (result)
        {
            JSON2ApexBatchList obj = JSON2ApexBatchList.parse(json);
            wrapPageList = new List<wrapPage>();
            
            for (Integer i = 0; i < obj.pageNames.size(); i++) {      
                wrapPageList.add(new wrapPage(obj.pageNames[i].pageId, obj.pageNames[i].pageName));
            }        
            
        } */
        wrapPageList = new List<wrapPage>();
        wrapPageList.add(new wrapPage('111','sample_name'));
        system.debug('componentController:retrievePages-->' +wrapPageList);
        return wrapPageList;
    }
   
    
    
  

    public class wrapPage {
        //@auraEnabled public String pageId {get; set;}
        //@auraEnabled public String pageName {get; set;}
        @auraEnabled public String value {get; set;}
        @auraEnabled public String options {get; set;}
        @auraEnabled public Boolean selected {get; set;}
        
        public wrapPage(String strpageId, String strpageName) {
            //pageId = strpageId;
            //pageName = strpageName;
            value = strpageId;
            options = strpageName;
            selected = false;
        }
    }
    
    
}