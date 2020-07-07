import SectionModel  = require('../models/section');
import {ISection} from "../models/section";
const express = require('express');
const Router = express.Router();


Router.post('/', async  (req: any, res: any ) => {
  const body = req.body;
  const entry = new SectionModel.SectionModel(body);
  await entry.save((err, product: ISection )=> {
    try{
      if(!product){
        res.status(201).send({message: 'No Object Created'});
      }
      console.log(product)
      res.status(201).send({entry: product, message: 'Section Created'})
    }catch (e) {
      if(err){
        res.status(501).send({error: err, message: ' No Section Created'});
      }
    }
  });
  console.log('==== Section Endpoint =====');
  console.log('Recieved Body');
  console.log(body);

});
Router.get('/:id', async (req: any , res: any )=> {

  try{
    const id = req.params.id;
    console.log(id)
    const section = await SectionModel.SectionModel.findById(id);
    console.log(section)
    if(!section){
      res.status(201).send({message: ' No entrys Found for ID: ' + id})
    }
    res.status(201).send(section);

  }catch (e) {
    res.status(501).send({message: 'error', error: e});
  }

});
Router.get('/',async (req: any, res: any ) => {
  try{
    const result = await SectionModel.SectionModel.find({});
    if(result.length == 0 ){
      res.status(201).send({message: ' No entrys Found'})
    }
    res.status(201).send({entrys: result});

  }catch (e) {
    res.status(501).send({message: 'error', error: e});
  }
  });

Router.delete('/', async (req: any, res: any ) => {

  try{
    await SectionModel.SectionModel.deleteMany({})
    res.status(201).send({message:' Deleted ALl sections'});
  } catch (e) {
    res.status(501).send({message: 'error', error: e});
  }

});



export default Router;
