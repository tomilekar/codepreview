import {Course} from "../../../frontend/src/models/course.model";

const app = require('express');
const Router = app.Router();
import authentication from './auth/authentication.route';
import mongoose = require("mongoose");
mongoose.set('debug',true);

import {CourseModel} from '../models/course';
import validatex from '../validators/course';
import cors = require("cors");



Router.post('/',authentication.isUserLoggedIn,async (req: any, res: any) => {
  const body = req.body;
  const lection = body['sections'][0]['lections'][0];
  console.log('Lection========');
  console.log(lection);
  const entry = new CourseModel(body);

  console.log('HEADERS');
  console.log(req.headers)


  try {
    await entry.save({validateBeforeSave: true}, (err:any, product: any) =>{
      console.log('Created Object');
      console.log(product);
    }).catch((x)=> console.log(x));
    res.status(201).send({entry, message: 'Entry Added'});

  } catch (e) {

    res.status(500).send({e, message: ' error'});
  }

});


Router.get('/', async (req: any, res: any) => {
  await CourseModel.find({}, (err,documents: mongoose.Document[])=>{
    try{
      if(err){
        res.status(201).send({message: 'Error', error: err});
        return
      }
      if(documents.length == 0){
        res.status(201).send({message: 'Couldnt find any Courses!'});
        return
      }
      console.log(documents);
      res.status(201).send(JSON.stringify(documents));
    }catch (e) {
      res.status(500).send(e);
    }

  }).catch((x)=> console.log(x));
});


Router.get('/:id', async (req: any, res: any) => {
  const id = req.params['id'];
  console.log(id);


  try {
    const course = await CourseModel.findById(id);
    if (!course) {
      return res.status(201).send({course, message: 'NO entry found'});

    }
    res.status(201).send(course);
  } catch (e) {
    res.status(500).send({e, message: ' Error'});
  }finally{

  }

});

Router.delete('/:id', async (req: any, res: any) => {
  const id = req.params['id'];
  await CourseModel.deleteOne({_id: id});
  try {
    res.status(201).send({message: 'Deleted object with id' + id});
  } catch (e) {
    res.status(500).send({e, message: 'error'});
  }


});


export default Router;
