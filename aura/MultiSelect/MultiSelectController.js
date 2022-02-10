({
    init: function (component,event,helper) {
        var items = [{
            "label": component.get("v.pageName"),
            "name": "1",
            "expanded": true,
            "items": []
        }];
        component.set('v.items', items);
        helper.helperMethod(component,event,helper);
    },
    handleChange:function(component,event,helper){
        console.log(JSON.stringify(component.get("v.value")));
        let selected=component.get("v.value");
        let childRecs=[];
        for(let i=0;i<selected.length;i++){
            let childItem={
                "label": selected[i],
                "name": i*10,
                "expanded": true,
                "items" :[]
            };
            childRecs.push(childItem);
        }
        let master=component.get("v.items");
        console.log(JSON.stringify(component.get("v.items")));
        master[0]["items"]=childRecs;
        component.set('v.items', master);
    }
});