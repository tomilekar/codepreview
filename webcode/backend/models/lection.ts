import mongoose = require('mongoose');
import {resourceSchema, IResource} from '../models/resource';

export interface ILection extends mongoose.Document {
  // id: number;
  title: string;
  video: IResource;
  attachments: IResource[];
}

export const lectionSchema = new mongoose.Schema({
  // id: { type: Number, trim: true, required: true },
  title: {
    type: String,
    trim: true,
    required: true,
  },
  video: {
    type: resourceSchema,
    required: true,
  },
  attachments: {
    type: [resourceSchema],
  },
});

export const lectionModel = mongoose.model<ILection>('Lection', lectionSchema);
