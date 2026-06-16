import maplibregl from 'maplibre-gl';
import { DARK_STYLE_URL } from './map.constants';

export function createMap(container: HTMLElement): maplibregl.Map {
  return new maplibregl.Map({
    container,
    style: DARK_STYLE_URL,
    center: [-56.1645, -34.9011],
    zoom: 7,
  });
}
