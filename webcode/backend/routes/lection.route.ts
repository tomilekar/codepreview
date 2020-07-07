const router = require('express').Router();
import { lectionModel } from '../models/lection';
import validate from '../validators/lection';

//post entry to database
router.post('/', validate.lection, async (req: any, res: any) => {
  const entry = new lectionModel(req.body);
  try {
    await entry.save();
    res.status(201).send({ entry, message: 'entry added' });
  } catch (err) {
    res.send(500).send(err);
  }
});

//get all entries
router.get('/', async (req: any, res: any) => {
  try {
    const result = await lectionModel.find({});
    if (result.length === 0) return res.status(200).send('No entries found');
    res.status(200).send(result);
  } catch (err) {
    res.status(500).send(err);
  }
});

//get entry by ID
router.get('/:id', async (req: any, res: any) => {
  try {
    const entry = await lectionModel.findById(req.params.id);
    if (!entry) res.status(400).send('No entry found');
    res.status(200).send(entry);
  } catch (err) {
    res.status(500).send(err);
  }
});

// delete entry by ID
router.delete('/:id', async (req: any, res: any) => {
  try {
    const entry = await lectionModel.findByIdAndDelete(req.params.id);
    if (!entry) res.status(400).send('No entry found');
    res.status(201).send({ entry, message: 'entry deleted' });
  } catch (err) {
    res.status(500).send(err);
  }
});

export default router;
