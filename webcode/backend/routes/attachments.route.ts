const router = require('express').Router();
import multer = require('multer');


const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, 'public/attachments')
  },
  filename: function (req, file, cb) {
    cb(null,  Date.now() + '-' + file.originalname)
  }
});

const upload = multer({storage:storage});

router.post('/', upload.single('attachment'), async (req: any, res: any) => {
  const message = req.body['message'];
  const file  = req.file;
  console.log(req);
  console.log(file);
  const mimeType = file['mimetype'].toString().split('/')[1];
  const path = file['path'] + '.' + mimeType;
  const video = {path: path, message: message};
  console.log(video)
  res.status(201).send(JSON.stringify(video));
});

export default router;
