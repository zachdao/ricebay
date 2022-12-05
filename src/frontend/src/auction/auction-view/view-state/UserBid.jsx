import React, { useCallback, useContext, useState } from 'react';
import { UserContext } from '../../../user.context';
import {
    Button,
    Content,
    Dialog,
    Flex,
    Heading,
    DialogTrigger,
    NumberField,
} from '@adobe/react-spectrum';
import { usePostWithToast } from '../../../http-query/use-post-with-toast';

export const UserBid = ({ auction, refresh, ...otherProps }) => {
    const [bid, setBid] = useState(auction?.userBid?.bid || 0);

    const isValid = useCallback(() => {
        return auction.userBid.maxBid > 0
            ? bid <= auction.userBid.maxBid
            : true;
    }, [bid]);

    const updateBid = usePostWithToast(
        `/auctions/${auction.id}/updateBid`,
        null,
        [auction],
        { message: 'Bid updated!' },
        { message: 'Failed to update bid!' },
        () => refresh(),
    );
    const isHighest = auction.userBid.bid === auction.currentBid;
    return (
        <>
            <DialogTrigger type="popover">
                <Button
                    alignSelf="center"
                    variant="cta"
                    isDisabled={isHighest}
                    {...otherProps}
                >
                    Increase Bid{' '}
                    {auction.userBid.maxBid > 0 &&
                        `(Max: $${auction.userBid.maxBid})`}
                </Button>
                <Dialog size="M">
                    <Heading>Increase your bid</Heading>
                    <Content>
                        <Flex
                            direction="rows"
                            justifyContent="space-between"
                            alignItems="end"
                            marginTop="size-100"
                            gap="size-50"
                        >
                            <NumberField
                                validationState={
                                    !isValid() ? 'invalid' : undefined
                                }
                                label="Your bid"
                                width="100%"
                                isRequired
                                value={bid}
                                onChange={setBid}
                                minValue={
                                    (auction.currentBid || auction.minimumBid) +
                                    (auction.bidIncrement || 0)
                                }
                                step={auction.bidIncrement || 0.5}
                                hideStepper
                                errorMessage="Bid is higher than your established max bid"
                                formatOptions={{
                                    style: 'currency',
                                    currency: 'USD',
                                    currencyDisplay: 'symbol',
                                }}
                            />
                            <Button
                                width="size-2000"
                                variant="cta"
                                onPress={() => updateBid({ bid })}
                            >
                                Update bid
                            </Button>
                        </Flex>
                    </Content>
                </Dialog>
            </DialogTrigger>
            <div
                style={{
                    color: isHighest ? 'green' : 'red',
                    width: '100%',
                    gridColumnStart: '1',
                    gridColumnEnd: '3',
                    marginTop: '5px',
                    textAlign: 'center',
                }}
            >
                {isHighest
                    ? 'You have the highest bid'
                    : 'You have been outbid'}
            </div>
        </>
    );
};
