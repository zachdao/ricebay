import React, { useEffect, useState } from 'react';
import { Text } from '@adobe/react-spectrum';

export const AppName = () => {
    const [darkTheme, setDarkTheme] = useState(
        window.matchMedia('(prefers-color-scheme: dark)'),
    );
    useEffect(() => {
        setDarkTheme(window.matchMedia('(prefers-color-scheme: dark)'));
    }, []);
    return <Text>{darkTheme.matches ? 'RiceRoad' : 'RiceBay'}</Text>;
};
