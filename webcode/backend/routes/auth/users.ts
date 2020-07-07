const router = require('express').Router();
const bcrypt = require('bcrypt');
const jsonwebToken = require('jsonwebtoken');
const fs = require('fs');
import User, {IUser} from '../../models/users';
import  {CourseModel, ICourse} from "../../models/course";
import validate from '../../validators/user';
import authentication from './authentication.route';
const mongoose = require('mongoose');

router.post('/register', validate.registration, async (req: any, res: any) => {
  const user = new User(req.body);

  try {
    let password = user.password;
    const hashedPassword = await new Promise((resolve,reject) => {
      bcrypt.hash(password,10,async (err:Error, encrypted: string) => {
        if(err) { res.status(500).send(err)}
        if(encrypted){
          resolve(encrypted);
        }
      })
    }).then(async (x) => {
      user.password = x as string;
      await user.save();
      res.status(201).send(JSON.stringify(user));

    });

  } catch (err) {
    console.log(err);
    res.status(400).send(err);
  }
});

router.post('/login', validate.user, async (req: any, res: any) => {
  const { email, password } = req.body;


  try {
    let checkUser = new Promise(async (resolve_1,reject_1) => {
        const user: IUser | null = await User.findOne({email: email});
        if (!user) return res.status(400).send('No user found');
        const hashPassword = await new Promise((resolve_2,reject_2) => {
          bcrypt
            .hash(password, 10,
              async (error:Error, encrypted:string)=> {
                if(error){
                  res.status(501).send(error);
                }
                if(encrypted){
                  resolve_2(encrypted)
                }
              })
        });
        if(!user.password === hashPassword) return res.status(201).send('Passowrd dosnt match');
       const privateKey = fs.readFileSync('./keys/private_key.pki');
       const publicKey = fs.readFileSync(('./keys/public_key.pem'));
        const data = {
          email: user.email,
          auth: user.username
        }
        const token = jsonwebToken.sign(data,privateKey,{
          algorithm:'HS256',
        });
        console.log(token);
      //  const verify = jsonwebToken.verify(token, privateKey, {
        //  algorithms: 'HS256'
        //})

      res.status(201).send(JSON.stringify({token: token,id: user._id}));


      }
    )
  } catch (err) {
    res.status(500).send(err);
  }
});

router.patch('/:id', async (req:any, res: any) => {
  const id = req.params['id'];
  const courseid = req.body['course'];
  console.log(req)

  console.log(id);
  console.log(courseid)
  const user = await User.findOne({_id: id});
  const course = await CourseModel.findOne({_id: courseid})
  if(user && course){

    console.log(course)
    user.courses.push(course._id);
    await user.save();
    user.markModified('courses');
    console.log(user);
    res.send(JSON.stringify(user));
  }
})
export default router;
