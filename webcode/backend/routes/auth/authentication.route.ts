const jsonwebtoken = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const bodyparser = require('body-parser');
const express = require('express');
const Router = express.Router();
const fs = require('fs');
Router.use(bodyparser.json({}));


export default {
  isUserLoggedIn: async (req: any, res: any, next: any) => {
    const bearerToken: string = req.headers['authorization'];
    let token = bearerToken.replace("Bearer", "").trim();
    if(bearerToken === ""){ res.status(201).send("PLEASE LOGIN FIRST")}
    const privateKey = fs.readFileSync('./keys/private_key.pki');

    const verified = jsonwebtoken.verify(token,privateKey, {algorithms: ["HS256"]});
    console.log(verified);
    if(verified === ""){
      console.log('NOT GOOD TOKEN')
    res.status(500).send('NOT GOOD TOKEN')
    }
    next();
  }
};
