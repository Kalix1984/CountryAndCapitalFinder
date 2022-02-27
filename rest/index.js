const mongoose = require('mongoose');
const fs = require('fs');
const express = require('express');
const { Collection } = require('mongodb');
const app = express();
const port = 3000
app.use(express.json());
app.use(express.urlencoded({extended: true}));

main().catch(err => console.log(err));

async function main() {

    mongoose.connect("mongodb://mongo:27017/mydb");

    const countrySchema = new mongoose.Schema({
      
    });
    
    //Country model
    const Country = mongoose.model('Country', {
        name: {type: String},
        capital: {type: String}
    });

    //ReadIn json
    let rawdata = fs.readFileSync('countries.json');
    let contries = JSON.parse(rawdata);
   
    //init collection
    Country.insertMany(
        contries
    ).then(function(){
        console.log("Data inserted");
    }).catch(function(err){
        console.log(err);
    });
    
    //Mapping
    app.get('/country/:name', (req, res) => {
        console.log("GET /county/", req.params.name);
        const query = req.params.name;
        const match_result = query.match('^[a-zA-Z]+$');
                
        if (match_result == null){
            return res.status(404).json({"message": "Not valid parameter"});
        }
        
        Country.find({name: { $regex: '.*' + query + '.*', '$options' : 'i'}}, function(_, result){
            if (result.length==0){
                return res.status(404).json({"message": "Not found"});
            }

            res.status(200).send(result);
        });
    });

    app.get('/capital/:name', (req, res) => {
        console.log("GET /capital/", req.params.name);
        const query = req.params.name;
        const match_result = query.match('^[a-zA-Z]+$');

        if (match_result == null){
            return res.status(404).json({"message": "Not valid parameter"});
        }
        
        Country.find({capital: { $regex: '.*' + query + '.*', '$options' : 'i'}}, function(_, result){
            if (result.length==0){
                return res.status(404).json({"message": "Not found"});
            }

            res.status(200).send(result);
        });
    });

    app.get('/countries', (req, res) => {
        console.log("GET /countries");
        
        Country.find({}, function(_, result){
            res.status(200).send(result);
        });
    });

    app.listen(port, () =>{
        console.log('Server running on port:', port)
    })
}