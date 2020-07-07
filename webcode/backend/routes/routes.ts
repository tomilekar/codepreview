import usersRoute from './auth/users';
import lectionRoute from '../routes/lection.route';
import videoRoute from '../routes/video.route';
import attachRoute from '../routes/attachments.route';
import courseRoute from '../routes/course.route';
import imageRouter from '../routes/image.route';
import sectionRouter from '../routes/section.route';
import * as path from "path";
import express = require("express");




export default (app: any) => {


  app.use(express.static('public/'));
  app.use(express.static('public/video/'));
  app.use(express.static('public/attachments/'));
  app.use(express.static('public/images/'));
  app.use('/users', usersRoute);
  app.use('/course', courseRoute);
  app.use('/lection', lectionRoute);
  app.use('/video', videoRoute);
  app.use('/attachments', attachRoute);
  app.use('/image', imageRouter)
  app.use('/section', sectionRouter)
};
