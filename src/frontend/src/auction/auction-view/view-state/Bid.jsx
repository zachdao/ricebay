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

export const Bid = ({ auction, refresh, ...otherProps }) => {
    const [bid, setBid] = useState(auction?.userBid?.bid || 0);
    const [maxBid, setMaxBid] = useState(auction?.userBid?.maxBid || undefined);

    const isValid = useCallback(() => {
        return makeBid > 0 ? bid <= maxBid : true;
    }, [bid, maxBid]);

    const makeBid = usePostWithToast(
        `/auctions/${auction.id}/placeBid`,
        null,
        [auction],
        { message: 'Bid placed!' },
        { message: 'Failed to place bid!' },
        () => refresh(),
    );

    return (
        <DialogTrigger type="popover">
            <Button alignSelf="center" variant="cta" {...otherProps}>
                Bid
            </Button>
            <Dialog size="M">
                <Heading>Place your bid</Heading>
                <Content>
                    <Flex
                        direction="rows"
                        justifyContent="space-between"
                        alignItems="end"
                        marginTop="size-100"
                        gap="size-50"
                    >
                        <NumberField
                            validationState={!isValid() ? 'invalid' : undefined}
                            label="Your bid"
                            isRequired
                            value={bid}
                            onChange={setBid}
                            minValue={
                                (auction.currentBid || auction.minimumBid) +
                                (auction.bidIncrement || 0)
                            }
                            step={auction.bidIncrement || 0.5}
                            hideStepper
                            errorMessage="Your bid must be lower than your max bid"
                            formatOptions={{
                                style: 'currency',
                                currency: 'USD',
                                currencyDisplay: 'symbol',
                            }}
                        />
                        <NumberField
                            validationState={!isValid() ? 'invalid' : undefined}
                            label="Max bid"
                            value={maxBid}
                            onChange={setMaxBid}
                            hideStepper
                            minValue={
                                bid || auction.currentBid || auction.minimumBid
                            }
                            step={auction.bidIncrement || 0.5}
                            errorMessage="Your max bid must be higher than your bid"
                            formatOptions={{
                                style: 'currency',
                                currency: 'USD',
                                currencyDisplay: 'symbol',
                            }}
                        />
                        <Button
                            width="size-2000"
                            variant="cta"
                            isDisabled={!isValid()}
                            onPress={() => makeBid({ bid, maxBid })}
                        >
                            Make bid
                        </Button>
                    </Flex>
                </Content>
            </Dialog>
        </DialogTrigger>
    );
};
