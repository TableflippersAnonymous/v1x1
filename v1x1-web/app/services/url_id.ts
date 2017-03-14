import * as ByteBuffer from "bytebuffer";
export class UrlId {
  private id: ByteBuffer;

  constructor(id: ByteBuffer) {
    this.id = id;
  }

  static fromApi(id: string): UrlId {
    return UrlId.fromHex(id.replace(/-/g, ''));
  }

  static fromHex(id: string): UrlId {
    return new UrlId(ByteBuffer.fromHex(id));
  }

  static fromUrl(id: string): UrlId {
    return new UrlId(ByteBuffer.fromBase64(id));
  }

  toUrl(): string {
    return this.id.toBase64().replace(/=/g, '');
  }

  toHex(): string {
    return this.id.toHex();
  }

  toApi(): string {
    let hexString = this.toHex();
    return hexString.substr(0, 8) + '-' +
        hexString.substr(8, 4) + '-' +
        hexString.substr(12, 4) + '-' +
        hexString.substr(16, 4) + '-' +
        hexString.substr(20, 12);
  }
}
