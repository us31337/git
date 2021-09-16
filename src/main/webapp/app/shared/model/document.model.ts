export interface IDocument {
  id?: number;
  name?: string;
  path?: string | null;
  uuid?: string | null;
}

export const defaultValue: Readonly<IDocument> = {};
