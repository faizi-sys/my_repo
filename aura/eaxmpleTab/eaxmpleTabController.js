({
    doinit : function(component, event, helper) {
        var typeOptions = [
            { label: "Option1", value: "Option1" },
            { label: "Option2", value: "Option2" },
            { label: "Option3", value: "Option3" }
        ];
        component.set("v.listOfOptions", typeOptions);
        
        let arrayVal=[];
        arrayVal.push({'title':'Text','display':'','type':'Option1','value':''});
        arrayVal.push({'title':'Password','display':'','type':'Option1','value':''});
        arrayVal.push({'title':'Click','display':'','type':'Option1','value':''});
        component.set("v.listOfNodes",arrayVal);
        var items = [
            { label: "Text", value: "Text",icon:"utility:download" },
            { label: "Password", value: "Password",icon:"utility:lock" },
            { label: "Click", value: "Click",icon:"utility:download" },
            { label: "Radio", value: "Radio",icon:"utility:download" }
        ];
        component.set("v.listOfMenus", items);
    },
    showRawJSON: function(component, event, helper) {
        alert(JSON.stringify(component.get("v.listOfNodes")));
    },
    handleMenuSelect:function(component, event, helper){
        var selectedMenuItemValue = event.getParam("value");
        let arrayVal=component.get("v.listOfNodes");
        arrayVal.push({'title':selectedMenuItemValue,'display':'','type':'Option1','value':''});
        component.set("v.listOfNodes",arrayVal);
    },
    showPanel:function(component, event, helper){
        if(component.get("v.showPanel")){
            component.set("v.showPanel",false);
        }else{
            component.set("v.showPanel",true);
        }
    },
    buttonClick:function(component, event, helper){
        let selectedValue=event.getSource().get("v.name")
        let arrayVal=component.get("v.listOfNodes");
        arrayVal.push({'title':selectedValue,'display':'','type':'Option1','value':''});
        component.set("v.listOfNodes",arrayVal);
    },
    handleShowModal: function(component, evt, helper) {
        var modalBody;
        $A.createComponent("c:MultiSelect", {
            pageName:component.get("v.pageName")
        },
           function(content, status) {
               if (status === "SUCCESS") {
                   modalBody = content;
                   component.find('overlayLib').showCustomModal({
                       header: "Test Case Editor",
                       body: modalBody,
                       showCloseButton: true,
                       cssClass: "mymodal",
                       closeCallback: function() {
                          // alert('You closed the alert!');
                       }
                   })
               }
           });
    }

})