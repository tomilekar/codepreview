const express = require('express');
const router =express.Router();
const fsx = require('fs')

import multer = require('multer');



const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, 'public/video')
  },
  filename: function (req, file, cb) {
    cb(null,  Date.now() + '-' + file.originalname)
  }
});

const upload = multer({storage:storage});
console.log(__dirname)
router.post('/', upload.single('video'), async (req: any, res: any) => {
  const message = req.body['message'];
  const file  = req.file;
  let path: string = file['path'];
  path = path.replace('public/', "");
  const video = {path: path, message: message};
console.log(path)
  res.status(201).send(JSON.stringify(video));
});

export default router;
