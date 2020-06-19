export var fr: any = {
    "SCHEDULER-GENERAL": {
        "ACTIONS": {
            "SAVE": "enregistrer",
            "CANCEL": "Cancel",
            "OK": "OK",
            "DELETE": "Delete",
            "CONFIRM": "Confirm",
            "SELECT": "Select",
            "PAUSE": "Pause",
            "BACK": "Back",
            "ACTIONS": "Actions",
        },
        "ERRORS": {
            "REQUIRED": "Field is required.",
            "LENGTH-EXCEEDING": "Length cannot be greater than {{length}}.",
            "LENGTH-SHORT": "Length cannot be less than {{length}}.",
            "INVALID-FORMAT": "Invalid format.",

        }
    },
    "JOBS": {
        "TITLE": "Jobs",
        "MESSAGES": {
            "CONFIRM": {
                "TRIGGER": "Are you sure you want to trigger this job right now?",
                "PAUSE": "Are you sure you want to pause this job?",
                "DELETE": "Are you sure you want to delete this job?",
            }
        },
        "FIELDS": {
            "NAME": "Name",
            "GROUP": "Group",
            "CLASS": "Class",
            "CLASS-PLACEHOLDER": "Select Job Class",
            "DESCRIPTION": "Description",
            "JOB-MAP-DATA": "Job Data",
            "LAST-EXECUTION-TIME": "Last Execution Time",
            "NEXT-EXECUTION-TIME": "Next Execution Time",
            "IS-DURABLE": "Durable",
        }
    },
    "JOB-DATA": {
        "KEY": "Key",
        "VALUE": "Value",
        "NEW-VALUE": "New Value",
    },
    "TRIGGERS":{
        "TITLE": "Triggers",
        "SELECT-JOB": "Select Job",
        "MESSAGES": {
            "TRIGGER-NEW-TITLE" : "New Trigger",
            "CONFIRM": {
                "PAUSE": "Are you sure you want to pause this trigger?",
                "DELETE": "Are you sure you want to delete this trigger?",
            }
        },
        "FIELDS": {
            "NAME": "Name",
            "GROUP": "Group",
            "JOB-NAME": "Job Name",
            "JOB-GROUP": "Job Group",
            "JOB-CLASS": "Job Class",
            "JOB-CLASS-PLACEHOLDER": "Select Job Class",
            "DESCRIPTION": "Description",
            "TYPE":"Type",
            "STATUS":"Status",
            "JOB-MAP-DATA": "Job Data",
            "LAST-EXECUTION-TIME": "Last Execution Time",
            "NEXT-EXECUTION-TIME": "Next Execution Time",
            "START-TIME":"Start Time",
            "START-DATE":"Start Date",
            "END-TIME":"End Time",
            "END-DATE":"End Date",
            "CRON-EXPRESSION":"Cron Expression",
            "REPEAT-INTERVEL":"Repeat Intervel",
            "REPEAT-INDEFINITELY":"Repeat Indefinitely",
            "REPEAT-COUNT":"Repeat Count",
        }
    },
    "EXECUTING-JOBS":{
        "TITLE": "Executing Jobs",
        "FIELDS": {
            "TRIGGER-NAME": "Trigger Name",
            "TRIGGER-GROUP": "Trigger Group",
            "JOB-NAME": "Job Name",
            "JOB-GROUP": "Job Group",
            "JOB-CLASS": "Job Class",
            "STATUS":"Status",
            "FIRE-TIME": "Last Execution Time",
            "NEXT-FIRE-TIME": "Next Execution Time",

        }
    },
    "EXECUTION-HISTORY":{
        "TITLE": "Execution History",
        "FIELDS": {
            "TRIGGER-NAME": "Trigger Name",
            "TRIGGER-GROUP": "Trigger Group",
            "JOB-NAME": "Job Name",
            "JOB-GROUP": "Job Group",
            "JOB-CLASS": "Job Class",
            "STATUS":"Status",
            "DURATION":"Duration",
            "FIRE-TIME": "Last Execution Time",
            "FINISHED-TIME": "Finished Time",

        }
    }
}