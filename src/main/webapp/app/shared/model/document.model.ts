import {IUser} from 'app/shared/model/user.model';

export interface IDocument {
  id?: number;
  name?: string;
  file?: object;
  path?: string | null;
  uuid?: string | null;
  changeTime?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IDocument> = {};
