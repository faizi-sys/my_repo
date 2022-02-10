({
	doinit : function(component, event, helper) {
		let arrayVal=[];
        arrayVal.push({'title': 'Text','Display':'', 'Type':'','Value':''});
        arrayVal.push({'title': 'Password','Display':'', 'Type':'','Value':''});
        arrayVal.push({'title': 'Click','Display':'', 'Type':'','Value':''});
        arrayVal.push({'title': 'Radio Button','Display':'', 'Type':'','Value':''});
        arrayVal.push({'title': 'Button','Display':'', 'Type':'','Value':''});
        arrayVal.push({'title': 'Click','Display':'', 'Type':'','Value':''});
        component.set("v.listofNodes",arrayVal);
    },
    showRawJSON : function(component, event, helper){
        alert(JSON.stringify(component.get("v.listofNodes")));
	}
})