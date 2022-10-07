'use strict';

//App to display Final Project UI
var app;
let updateIntervalId;

/**
 * An optional function to create functions that are dependent on a particular view component.
 * Modify or delete this function to suit the needs of your application.
 * @param aViewComponent A UI component upon which the returned functionality will be based.   Add more parameters if needed.
 * @returns An object whose methods are functions dependent on the given view component
 */
function createApp(aViewComponent) {

    // TODO Implement view-side utility functions that depend on the given view component.

    // Return a dictionary with fields set to the utility functions, i.e. an object whose methods are the utility functions
    return {
        // TODO Add functions defined above as entries here.
    };
}


window.onload = function () {
    // Your app may not need this list if there's no component-specific functionality required.
    app = createApp(document.querySelector("aViewComponent_name"));

    // TODO Configure the UI, e.g. button listners, etc.

};

// TODO Add any additional functions or global variables that might be needed.
