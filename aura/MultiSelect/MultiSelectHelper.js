({
    helperMethod : function(component,event,helper) {
        // create a one-time use instance of the serverEcho action
        // in the server-side controller
        var action = component.get("c.makeCall");
        
        
        // Create a callback that is executed after 
        // the server-side action returns
        action.setCallback(this, function(response) {
            var state = response.getState();
            if (state === "SUCCESS") {
                // Alert the user with the value returned 
                // from the server
                console.log("From server: " + JSON.stringify(response.getReturnValue()));
                let master=response.getReturnValue().pageNames;
                let childRecs=[];
                for(let i=0;i<master.length;i++){
                    let childItem={'label': master[i].pageName, 'value': master[i].pageName};
                    childRecs.push(childItem);
                }
               
                component.set('v.options', childRecs);
                
                // You would typically fire a event here to trigger 
                // client-side notification that the server-side 
                // action is complete
            }
            else if (state === "INCOMPLETE") {
                // do something
            }
                else if (state === "ERROR") {
                    var errors = response.getError();
                    if (errors) {
                        if (errors[0] && errors[0].message) {
                            console.log("Error message: " + 
                                        errors[0].message);
                        }
                    } else {
                        console.log("Unknown error");
                    }
                }
        });
        
        // optionally set storable, abortable, background flag here
        
        // A client-side action could cause multiple events, 
        // which could trigger other events and 
        // other server-side action calls.
        // $A.enqueueAction adds the server-side action to the queue.
        $A.enqueueAction(action);
    }
})