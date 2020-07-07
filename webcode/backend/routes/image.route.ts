const router = require('express').Router();
import multer = require('multer');


const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, 'public/images')
  },
  filename: function (req, file, cb) {
    cb(null,  Date.now() + '-' + file.originalname)
  }
});

const upload = multer({storage:storage});

router.post('/', upload.single('image'), async (req: any, res: any) => {
  const message = req.body['message'];
  const file  = req.file;
  console.log(req);
  console.log(file);
  let path: string = file['path'];
  path = path.replace("public/","");
  const image = {path: path, message: message};
  console.log(image)
  res.status(201).send(JSON.stringify(image));
});

export default router;
