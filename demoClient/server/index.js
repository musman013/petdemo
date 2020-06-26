require("dotenv").config();
const express = require("express");
const bodyParser = require("body-parser");
const CubejsServerCore = require("@cubejs-backend/server-core");
const fs = require("fs");
const path = require("path");
const cors = require("cors");
const app = express();
app.use(require("cors")());
app.use(bodyParser.json({ limit: "50mb" }));
const serverCore = CubejsServerCore.create();
serverCore.initApp(app);
const port = process.env.PORT || 4200;
app.listen(port, err => {
  if (err) {
    console.error("Fatal error during server start: ");
    console.error(err.stack || err);
  }
  console.log(`🚀 Cube.js server is listening on ${port}`);
});
app.use(cors());
app.post("/saveschema", (req, res) => {
  const path = "schema/" + req.body.fileName;
  fs.writeFile(path, req.body.content, function(err) {
    if (err) {
      return console.log(err);
    }
    // console.log("Updated Schems!");
    res.json({ message: "Updated Schema!" });
  });
});
app.get("/test", (req, res) => {
  console.log("api working");
  res.send("api working");
});

const aggregations = [
  "sum",
  "avg",
  "min",
  "max",
  "runningTotal",
  "count",
  "countDistinct",
  "countDistinctApprox"
];
const time_aggregations = ["min", "max"];
const general_aggregations = ["count", "countDistinct", "countDistinctApprox"];
app.get("/generateAggregatedMeasures", (req, res) => {
  fs.readdirAsync("./schema").then(filenames => {
    //console.log(filenames);
    filenames.forEach(fileName => {
      console.log("about to read file");
      getFile(`./schema/${fileName}`, "utf8").then(function(data) {
        //var measures = getJsonFromString("measures",data);
        var measures = "";
        var dimensions = getJsonFromString("dimensions", data);
        var dimensionKeys = Object.keys(dimensions);
        dimensionKeys.forEach(dimension => {
          aggregations.forEach(aggregation => {
            if (
              dimensions[dimension].type == "number" ||
              (dimensions[dimension].type == "time" &&
                time_aggregations.indexOf(aggregation) > -1) ||
              general_aggregations.indexOf(aggregation) > -1
            ) {
              measures += `\t\t${aggregation}_${dimensions[dimension].sql}: {\n`;
              measures += `\t\t\tsql: '${dimensions[dimension].sql}',\n`;
              measures += `\t\t\ttype: '${aggregation}'\n`;
              measures += `\t\t},\n`;
            }
          });
        });
        var appendPoint = findFirstOccurence(data, "measures");
        var endPoint = findSecondLastOccurence(data);
        var file_content = data.substring(endPoint);
        var file = fs.openSync(`./schema/${fileName}`, "r+");
        var bufferedText = new Buffer(`\n${measures}` + file_content);
        fs.writeSync(file, bufferedText, 0, bufferedText.length, appendPoint);
        fs.close(file);
        //measures = measures.replace(/\\\//g, "/");
      });
    });
    res.send("done");
  });
});

// make Promise version of fs.readdir()
fs.readdirAsync = function(dirname) {
  return new Promise(function(resolve, reject) {
    fs.readdir(dirname, function(err, filenames) {
      if (err) reject(err);
      else resolve(filenames);
    });
  });
};

// make Promise version of fs.readFile()
fs.readFileAsync = function(filename, enc) {
  return new Promise(function(resolve, reject) {
    fs.readFile(filename, enc, function(err, data) {
      if (err) reject(err);
      else resolve(data);
    });
  });
};

// utility function, return Promise
function getFile(filename) {
  return fs.readFileAsync(filename, "utf8");
}

function getJsonFromString(startStr, str) {
  var startIndex = str.indexOf(startStr);
  var newStr = "";
  startIndex = startIndex + startStr.length + 2;
  var count = 0;
  var i = 0;
  do {
    newStr = newStr + str[startIndex + i];
    if (str[startIndex + i] == "{") {
      count++;
    } else if (str[startIndex + i] == "}") {
      count--;
    }
    i++;
  } while (count > 0);

  if (startStr == "measures") {
    var drillMembersStartIndex = newStr.indexOf("drillMembers");
    var drillMembersLastIndex = 0;
    count = 1;
    i = 0;
    if (drillMembersLastIndex > -1) {
      drillMembersStartIndex = drillMembersStartIndex + 15;
      drillMembersLastIndex = drillMembersStartIndex;
      while (count > 0) {
        if (newStr[drillMembersStartIndex + i] != "]") {
          drillMembersLastIndex++;
        } else if (newStr[drillMembersStartIndex + i] == "]") {
          count--;
        }
        i++;
      }
      newStr = newStr.replace(
        newStr.substring(drillMembersStartIndex, drillMembersLastIndex),
        ""
      );
    }
  }
  newStr = eval("(" + newStr + ")");
  return newStr;
}

function findFirstOccurence(str, data) {
  console.log(data, str.length);
  var measureIndex = str.indexOf(data);
  measureIndex = measureIndex + data.length + 3;
  console.log(measureIndex);
  console.log(str[measureIndex]);
  return measureIndex;
}

function findSecondLastOccurence(data) {
  var measureIndex = data.indexOf("measures");
  var startIndex = measureIndex + 10;
  var secondLastOccurence = 0;
  var count = 1;
  var i = 1;
  /* console.log(startIndex);  
  console.log(data[startIndex]);
  console.log("count",count); */
  while (count > 0) {
    if (data[startIndex + i] == "{") {
      count++;
      //console.log("increasing count",startIndex,+i);
    } else if (data[startIndex + i] == "}") {
      count--;
      //console.log(count);
    }
    if (data[startIndex + i] == "}" && count == 1) {
      //console.log("assigning",startIndex+i)
      secondLastOccurence = startIndex + i;
    }
    i++;
  }
  /* console.log(secondLastOccurence);
  console.log(data[secondLastOccurence]); */
  return secondLastOccurence + 1;
}
// CUBEJS_DB_HOST=localhost
// CUBEJS_DB_NAME=SampleTravelData
// CUBEJS_DB_USER=root
// CUBEJS_DB_PASS=hiroot
// CUBEJS_WEB_SOCKETS=true
// CUBEJS_DB_TYPE=mysql
// CUBEJS_API_SECRET=912e57285ccda22e0e0524b3b8407dfd4f66d2be9e4a1321cdb4c350d7acf0670aaa46ea702e3913120176876ab54c19160452a31aef6478d26e6bfcc7854958

// CUBEJS_DB_HOST=localhost
// CUBEJS_DB_NAME=ecom
// CUBEJS_DB_USER=tejavarma
// CUBEJS_DB_PASS=admin
// CUBEJS_WEB_SOCKETS=true
// CUBEJS_DB_TYPE=postgres
// CUBEJS_API_SECRET=912e57285ccda22e0e0524b3b8407dfd4f66d2be9e4a1321cdb4c350d7acf0670aaa46ea702e3913120176876ab54c19160452a31aef6478d26e6bfcc7854958
