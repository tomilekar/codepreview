import mongoose from 'mongoose';
import {ILection, lectionSchema} from '../models/lection';

export interface ISection extends mongoose.Document {
  // id: number;
  title: string;
  duration: number;
  description: string;
  completed: boolean;
  lections: ILection;
}

export const sectionSchema = new mongoose.Schema({

  title: {type: String, trim: true, required: true},
  //duration: {type: Number, trim: true, required: true},
 // description: {type: String, trim: true, required: true},
  completed: {type: Boolean, default: false, required: true},
  lections: [lectionSchema]
});

export const SectionModel = mongoose.model<ISection>('Section', sectionSchema);
