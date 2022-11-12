import React from 'react';
import { render, screen } from '@testing-library/react';
import { Popover } from './Popover';
import { View } from '@adobe/react-spectrum';
import userEvent from '@testing-library/user-event';

describe('Popover', () => {
    describe('uncontrolled', () => {
        it('should start hidden', async () => {
            await render(
                <Popover triggerText="foobar">
                    <View>Some content</View>
                </Popover>,
            );
            expect(screen.getByText('foobar')).toBeVisible();
            expect(screen.getByText('Some content')).not.toBeVisible();
        });

        it('should start open on trigger press', async () => {
            const user = await userEvent.setup();
            await render(
                <Popover triggerText="foobar">
                    <View>Some content</View>
                </Popover>,
            );
            await user.click(screen.getByText('foobar'));
            expect(screen.getByText('Some content')).toBeVisible();
        });
    });
    describe('controlled', () => {
        it('should be hidden when isOpen is false', async () => {
            await render(
                <Popover triggerText="foobar" isOpen={false}>
                    <View>Some content</View>
                </Popover>,
            );
            expect(screen.getByText('foobar')).toBeVisible();
            expect(screen.getByText('Some content')).not.toBeVisible();
        });
        it('should be visible when isOpen is true', async () => {
            await render(
                <Popover triggerText="foobar" isOpen={true}>
                    <View>Some content</View>
                </Popover>,
            );
            expect(screen.getByText('foobar')).toBeVisible();
            expect(screen.getByText('Some content')).toBeVisible();
        });
        it('should call "onPress" when trigger is pressed', async () => {
            const pressSpy = jest.fn();
            const user = userEvent.setup();
            await render(
                <Popover triggerText="foobar" isOpen={false} onPress={pressSpy}>
                    <View>Some content</View>
                </Popover>,
            );
            await user.click(screen.getByText('foobar'));
            expect(pressSpy).toHaveBeenCalled();
        });
    });
});
